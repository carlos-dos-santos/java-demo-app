package com.example.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.os.services.core.api.models.dms.objects.DmsObject;
import com.os.services.core.api.models.dms.objects.DmsObjectProperty;
import com.os.services.core.api.models.dms.objects.transfer.ResultContainerDmsObject;


public class DmsObjectList {
    private RestTemplate restTemplate;

    public DmsObjectList(final RestTemplate restTemplate) {
            this.restTemplate = restTemplate;
    }

    public static <T> ResultContainerDmsObject createDmsObjectsForId(List<Map<String, Object>> metadata) {
        final ResultContainerDmsObject dmsObjectList = new ResultContainerDmsObject();

        metadata.forEach(item -> {
            final DmsObject dmsObject = new DmsObject();
            final Map<String, DmsObjectProperty> properties = dmsObject.getProperties();

            item.forEach((name, value) -> {
                properties.put(name, new DmsObjectProperty(value));
            });

            dmsObjectList.addObject(dmsObject);
        });

        return dmsObjectList;
    }


    public static <T> String toJson(final T data) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(data);
    }


    public ResponseEntity<ResultContainerDmsObject> post(final String url, final String authorization, ResultContainerDmsObject body) {
        final String URI = url;

        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", authorization);
        }};

        HttpEntity<ResultContainerDmsObject> request = new HttpEntity<>(body, headers);
        return null;
        //return restTemplate.exchange(URI, HttpMethod.POST, request, ResultContainerDmsObject.class);
    }


    public static void main(String... args) throws JsonProcessingException {
        List<Map<String, Object>> metadata = List.of(
            Map.of("system:objectId","cdc7095f-a5ce-486d-92a7-6d0955d969ee"),
            Map.of("system:objectId","98eeed73-5d93-4881-af9a-444e3f0a290d"));

        var dms = createDmsObjectsForId(metadata);

        final String authorization = "";
        HttpHeaders headers = new HttpHeaders() {{
            add("Authorization", authorization);
        }};

        HttpEntity<ResultContainerDmsObject> request = new HttpEntity<>(dms, headers);

       System.out.println(toJson(request));
    }
}
