##### build the project
    mvn clean package

##### build Docker image called java-demo-app
    docker build -t java-demo-app .
    
##### push image to repo 
    docker tag java-demo-app machamba/java-demo-app:v1.0.0
    

