#!/usr/bin/env bash

set -eu

function main() {
    SHORT_ARGS=n:,p:,d:,c:,h:,l
    LONG_ARGS=namespace:,port:,deployment:,container:,help:,list
    ARGS=$(getopt -a -n ${0##*/} --options ${SHORT_ARGS} --longoptions ${LONG_ARGS} -- "$@")

    if [ "$?" -ne 0 ] || [ "$#" -eq 0 ]; then
        help; exit 4
    fi

    eval set -- "${ARGS}"


    while true; do
        case "$1" in
            -c | --container)   container="$2";  shift 2 ;;
            -d | --deployment)  deployment="$2"; shift 2 ;;
            -n | --namespace)   namespace="$2";  shift 2 ;;
            -p | --port)        port="$2";       shift 2 ;;
            -l | --list)        list; exit ;;
            -h | --help)        help ;;
            --) shift; break ;;
            *) echo "Unexpected option: $1"; help; exit 3;;
        esac
    done

    if kubectl get deploy ${deployment} -n ${namespace} > /dev/null 2>&1; then
        patch ${deployment} ${container} ${namespace} ${port}
    fi
}


function help() {
    echo "Usage: ${0##*/} [ -l | --list] [ -c | --container] [ -d | --deployment] [ -n | --namespace ] [ -p | --port] [ -h | --help]"
    exit 2
}


function list() {
    printf "%-35s %-30s\n" DEPLOYMENT "CONTAINER(S)"
    for entry in $(kubectl get deployments -A 2>/dev/null | sed '1d' | awk '{ print $2":"$1}'); do
        deployment="${entry%:*}"; namespace="${entry##*:}"
        printf "%-35s %-30s" "${deployment}" $(kubectl get deployment "${deployment}" -n "${namespace}" -o=jsonpath='{.spec.template.spec.containers[*].name}')
        printf "\n"
    done
}


function patch() {
    DEPLOYMENT_NAME="${1}"
    CONTAINER_NAME="${2}"
    NAMESPACE="${3}"
    JVM_DEBUG_PORT="${4}"

    CONTAINER_PORT=5005


    CONTAINERS=($(kubectl get deployment "${DEPLOYMENT_NAME}" -o jsonpath='{.spec.template.spec.containers[*].name }'))

    for i in ${!CONTAINERS[@]}; do
        if [ ".${CONTAINERS[$i]}" = ".${CONTAINER_NAME}" ]; then

            QUERY_PORT=$(printf \
                '{.spec.template.spec.containers[?(@.name==\"%s\")].ports[?(@.containerPort==%d)].containerPort}' \
                ${CONTAINER_NAME} ${CONTAINER_PORT})

            if [ ".${CONTAINER_PORT}" != ".$(kubectl get deployment ${DEPLOYMENT_NAME}  -o jsonpath=${QUERY_PORT})" ]; then
                kubectl patch deployment "${DEPLOYMENT_NAME}" \
                    --type='json' \
                    -p='[{"op": "add", "path": "/spec/template/spec/containers/'"${i}"'/ports/-", "value": {"containerPort": 5005, "protocol": "TCP" }}]'
            fi
        fi
    done


    JAVA_DEBUG_AGENT='-Xdebug -agentlib:jdwp=transport=dt_socket,address=0.0.0.0:5005,server=y,suspend=n'
    kubectl patch deployment "${DEPLOYMENT_NAME}" \
        -p '{"spec":{"template":{"spec":{"containers":[{"name": "'"${CONTAINER_NAME}"'","env": [{"name":"JAVA_TOOL_OPTIONS","value": "'"${JAVA_DEBUG_AGENT}"'"}]}]}}}}'

    # add custom label
    kubectl patch deploy "${DEPLOYMENT_NAME}"  \
        -p '{"spec":{"template":{"metadata":{"labels":{"debug": "agentlib"}}}}}'


cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: Service
metadata:
  name: debug-${DEPLOYMENT_NAME}
  namespace: ${NAMESPACE}
spec:
  selector:
    debug: agentlib
  type: NodePort
  ports:
  - port: 5005
    name: debug
    targetPort: 5005
    nodePort: ${JVM_DEBUG_PORT}
    protocol: TCP
EOF
}

main "$@"; exit 0

