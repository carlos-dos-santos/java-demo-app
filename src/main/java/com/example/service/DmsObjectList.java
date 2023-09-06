package com.example.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.os.services.core.api.models.dms.objects.DmsObject;
import com.os.services.core.api.models.dms.objects.DmsObjectProperty;
import com.os.services.core.api.models.dms.objects.transfer.ResultContainerDmsObject;


public class DmsObjectList {
    //@Autowired
    private RestTemplate restTemplate = new RestTemplate();

    private List<Map<String, Object>> metadata;


    //public DmsObjectList(final RestTemplate restTemplate, final List<Map<String, Object>> metadata) {
    public DmsObjectList(final List<Map<String, Object>> metadata) {
        //this.restTemplate = restTemplate;
        this.metadata = metadata;
    }

    public static ResultContainerDmsObject requestBody(List<Map<String, Object>> metadata) {
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


    // public static List<Map<String, Object>> toMetadata(final List<String> objectIds) {
    //     List<Map<String, Object>> metadata = new ArrayList<Map<String, Object>>();
    //     objectIds.forEach(id -> { 
    //         metadata.add(Map.of("system:objectId", id)); 
    //     });
    //     return metadata;
    // }
    //

    private static List<Map<String, Object>> toMetadata(final List<String> objectIds) {
        List<Map<String, Object>> metadata = new ArrayList<Map<String, Object>>();
        objectIds.forEach(objectId -> {
            metadata.add(Map.of("system:objectId", objectId));
        });
        return metadata;
    }


    public static <T> String toJson(final T data) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(data);
    }


    public ResponseEntity<ResultContainerDmsObject> post(String url, String authorization) {
        final String URI = url;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);

        return restTemplate.exchange(URI, HttpMethod.POST,
            new HttpEntity<>(requestBody(metadata), headers), ResultContainerDmsObject.class);
    }


    public ResponseEntity<Map> postParams(String url, String authorization, Boolean greedy) {
        final String URI = url;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", authorization);

        final Map<String, ?> uriVariables = new HashMap<>() {{
            put("greedy", greedy);
        }};

        final String message = "abc";
        final String body = "{\"message\":\"" + message + "\"}";

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>() {{
            this.put("greedy", List.of("true"));
        }};

        // final URI uri = UriComponentsBuilder.fromHttpUrl(url)
        //     .queryParams(param)
        //     .build();



        final UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(URI)
            .queryParam("greedy", greedy)
            .build();

        return restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
    }



    public static void main(String... args) throws JsonProcessingException {

/*
        List<String> objectIds = List.of(
            "cdc7095f-a5ce-486d-92a7-6d0955d969ee", 
            "98eeed73-5d93-4881-af9a-444e3f0a290d");

        final RestTemplate restTemplate = new RestTemplate();
        DmsObjectList dmsList = new DmsObjectList(restTemplate, toMetadata(objectIds));

        final String url = "http://localhost:9191/api/meta";
        final String authorization = "XXXX";

        dmsList.post(url, authorization);
 */

        List<String> objectIds = List.of(
            "cdc7095f-a5ce-486d-92a7-6d0955d969ee", 
            "98eeed73-5d93-4881-af9a-444e3f0a290d");

        //final RestTemplate restTemplate = new RestTemplate();
        final DmsObjectList dmsList = new DmsObjectList(toMetadata(objectIds));

        final String url = "http://localhost:9191/api/params";
        final String authorization = "XXXX";

        dmsList.postParams(url, authorization, false);
    }
}
