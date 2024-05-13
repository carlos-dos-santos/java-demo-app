package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.DmsObjectPatchData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.os.services.core.api.models.dms.objects.DmsObject;
import com.os.services.core.api.models.dms.objects.DmsObjectProperty;
import com.os.services.core.api.models.dms.objects.transfer.ResultContainerDmsObject;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import lombok.*;

import static de.utils.Json.jsonToString;


@RestController
@RequestMapping("/api")
public class HelloWorldController {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    private long counter = 0;

    @RequestMapping("/hello")
    public String index() {
        System.out.println("0 bla bla");
        return "Hello, World!";
    }

    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<String> data(@RequestBody String data) {
        System.out.println("xxx: " + ++counter);
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/objects", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam("id") final List<String> id) {
        id.stream()
                 .forEach(System.out::println);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // @RequestMapping(value = "/objects", method = RequestMethod.POST)
    // public ResponseEntity<> delete(@RequestParam List<String> ids) {
    //      
    // }

    // @RequestMapping(value = "/objects", method = RequestMethod.POST)
    // public ResponseEntity<String> delete(@RequestParam("id") String id) {
    //     return new ResponseEntity<String>(id, HttpStatus.OK);
    // }

    @RequestMapping(value = "/object", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(
        @RequestParam(required = false, defaultValue = "true") Boolean greedy,
        @RequestBody List<String> objectIds) {

        objectIds.forEach(System.out::println);
        logger.info("greedy: {}", greedy);

        return new ResponseEntity<String>("bbababa", HttpStatus.OK);
    }

    @RequestMapping(value = "/objects/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable String id) {
        logger.info("id: {}", id);
        return new ResponseEntity<String>(id, HttpStatus.OK);
    }


    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<String> products(@RequestBody String data) {
        logger.info("data: {}", data);
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/meta", method = RequestMethod.POST)
    public ResponseEntity<String> meta(@RequestBody String data) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.readTree(data).toPrettyString());
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    @RequestMapping(value = "/params", method = RequestMethod.POST)
    public ResponseEntity<String> params(@RequestBody String data, @RequestParam() Boolean greedy) {
        System.out.println(">>> greedy " + greedy + " " + greedy.toString());
        System.out.println(">>> data" + data);
        return new ResponseEntity<String>(data, HttpStatus.OK);
    }

    // POST with payload
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntity<String> list(@RequestBody List<String> list) {
        if(list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>("List is not empty", HttpStatus.OK);
    }


    // @RequestMapping(value = "/objects", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<?> update(@RequestBody ResultContainerDmsObject dmsObjects) throws JsonProcessingException {
    //     System.out.println(toJson(dmsObjects));
    //     return new ResponseEntity<ResultContainerDmsObject>(dmsObjects, HttpStatus.OK);
    // }

    public static <T> String _to(final T data) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper.writeValueAsString(data);
    }


    private List<Map<String, Object>> toMetadata(final List<String> objectIds) {
        List<Map<String, Object>> metadata = new ArrayList<Map<String, Object>>();
        objectIds.forEach(objectId -> {
            metadata.add(Map.of("system:objectId", objectId));
        });
        return metadata;
    }

/*
    private ResultContainerDmsObject requestBody(List<Map<String, Object>> metadata) {
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
*/



    
    
    // public <P, T> void prepare(final Map<P, List<T>> data) {
    //      List<Map<String, Object>> meta = new ArrayList<Map<String, Object>>();
    //      final ResultContainerDmsObject dmsObjectList = new ResultContainerDmsObject();
    //
    //      data.forEach((patch, entry) -> {
    //          entry.forEach(object -> {
    //              meta.add(Map.of("system:objectId", object.id));
    //
    //              object.data.forEach((name, value) ->  {
    //                  meta.add(Map.of(name, value));
    //              }); 
    //
    //              final DmsObject dmsObject = new DmsObject();
    //              final Map<String, DmsObjectProperty> properties = dmsObject.getProperties();
    //
    //              meta.forEach(item -> {
    //                  item.forEach((name, value) -> {
    //                      properties.put(name, new DmsObjectProperty(value));
    //                  });
    //              });
    //
    //              dmsObjectList.addObject(dmsObject);
    //              meta.clear();
    //          });
    //      });
    // }

    // private record DmsObjectPatchData(String id, Map<String, Object> data) {
    // }
    //
    // public ResponseEntity<?> update(@RequestBody Map<String, List<DmsObjectPatchData>> body) {
    //      List<Map<String, Object>> meta = new ArrayList<Map<String, Object>>();
    //      final ResultContainerDmsObject dmsObjectList = new ResultContainerDmsObject();
    //
    //      body.forEach((patch, entry) -> {
    //         System.out.println(entry);
    //          entry.forEach(object -> {
    //              meta.add(Map.of("system:objectId", object.id()));
    //
    //              object.data().forEach((name, value) ->  {
    //                  meta.add(Map.of(name, value));
    //              }); 
    //
    //              final DmsObject dmsObject = new DmsObject();
    //              final Map<String, DmsObjectProperty> properties = dmsObject.getProperties();
    //
    //              meta.forEach(item -> {
    //                  item.forEach((name, value) -> {
    //                      properties.put(name, new DmsObjectProperty(value));
    //                  });
    //              });
    //
    //              dmsObjectList.addObject(dmsObject);
    //              meta.clear();
    //          });
    //      });
    //
    //     return new ResponseEntity<String>("metadata", HttpStatus.OK);
    // }

    //
    // public ResultContainerDmsObject processBody(final String body) throws JsonMappingException, JsonProcessingException {
    //
    //     List<Map<String, Object>> metadata = new ArrayList<Map<String, Object>>();
    //     final ResultContainerDmsObject dmsObjectList = new ResultContainerDmsObject();
    //
    //     record Data(String id, Map<String, Object> data) {}
    //
    //     ObjectMapper objectMapper = new ObjectMapper();
    //     Map<String, List<Data>> obj = objectMapper.readValue(body, new TypeReference<Map<String, List<Data>>>() {});
    //
    //     obj.forEach((patch, entry) -> {
    //         entry.forEach(object -> {
    //             metadata.add(Map.of("system:objectId", object.id));
    //
    //             object.data.forEach((name, value) ->  {
    //                 metadata.add(Map.of(name, value));
    //             }); 
    //
    //             final DmsObject dmsObject = new DmsObject();
    //             final Map<String, DmsObjectProperty> properties = dmsObject.getProperties();
    //
    //             metadata.forEach(item -> {
    //                 item.forEach((name, value) -> {
    //                     if(value.equals("null"))
    //                         properties.put(name, null);
    //                     else
    //                         properties.put(name, new DmsObjectProperty(value));
    //                 });
    //             });
    //
    //             dmsObjectList.addObject(dmsObject);
    //             metadata.clear();
    //         });
    //     });
    //
    //     return dmsObjectList;
    // }
    //

    //record TestData(String id, Map<String, Object> data) {}


    // @RequestMapping(value = "/objects", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    // public ResponseEntity<?> update(@RequestBody Map<String, List<DmsObjectPatchData>> body) throws JsonProcessingException {
    // //public ResponseEntity<?> update(@RequestBody DmsObjectPatchData body) throws JsonProcessingException {
    //     body.patches.forEach(e -> System.out.println(e.id + " " + e.data));
    //
    //     // final ObjectMapper mapper = new ObjectMapper();
    //     // mapper.enable(SerializationFeature.INDENT_OUTPUT);
    //     // final String json = mapper.writeValueAsString(body);
    //     // System.out.println(json);
    //
    //     final ResultContainerDmsObject dmsObjectList = new ResultContainerDmsObject();
    //
    //     body.patches.forEach(patch -> {
    //         final DmsObject dmsObject = new DmsObject();
    //         final Map<String, DmsObjectProperty> properties = dmsObject.getProperties();
    //
    //         properties.put("system:objectId", new DmsObjectProperty(patch.id));
    //
    //         patch.data.forEach((name, value) -> {
    //             if(value.equals("null")) {
    //                 properties.put(name, null);
    //             } else { 
    //                 properties.put(name, new DmsObjectProperty(value));
    //             }
    //         });
    //
    //         dmsObjectList.addObject(dmsObject);
    //     });
    //
    //
    //     final ObjectMapper mapper = new ObjectMapper();
    //     mapper.enable(SerializationFeature.INDENT_OUTPUT);
    //     final String json = mapper.writeValueAsString(dmsObjectList);
    //     System.out.println(" >>>>>>>>>>> \n" + json);
    //
    //     return new ResponseEntity<String>("metadata", HttpStatus.OK);
    // }

    // public ResponseEntity<?> myMethod(@RequestBody(description = "test body",
    //     content = { @Content(mediaType = "application/json",
    //         examples = @ExampleObject(
    //             name = "test", 
    //             summary = "test example",
    //             value = "{ \"key\" : \"value\"}"),
    //         schema = @Schema(example = "{ \"key\" : \"value\"}")) }) String request) {
    //
    //     return new ResponseEntity<String>("metadata", HttpStatus.OK);
    // }


    // @PostMapping(value = "/muuu", produces = "application/json", consumes = "application/json")
    // public ResponseEntity<?> addBook(
    //     @io.swagger.v3.oas.annotations.parameters.RequestBody(
    //         description = "Book to add.", required = true,
    //         content = @Content(
    //             examples = @ExampleObject(
    //                 name = "test",
    //                 value = "[{\"name\":\"abc\"}]" 
    //             )
    //         ))
    //     @org.springframework.web.bind.annotation.RequestBody String book) {
    //     return new ResponseEntity<>("test", HttpStatus.OK);
    // }

}
