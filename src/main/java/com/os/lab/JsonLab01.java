package com.os.lab;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// "objects": [
//     {
//         "properties": {
//             "system:objectId": {
//                 "value": "cdc7095f-a5ce-486d-92a7-6d0955d969ee"
//             }
//         }
//     },
// {
//
public class JsonLab01 {
    public static void main(String args[]) throws Exception {
        //objectNode();
        //objectArrayNode();
        // body("903f2ae8-2cfc-476c-8386-55c6811e41da");
        testNested();
    }

    public static Map<String, Object> dmsProp(final String objectId) {
        Map<String, Object> properties = new HashMap<>() {{
            this.put("properties", new HashMap<>() {{ 
                this.put("system:objectId", new HashMap<>() {{ 
                    this.put("value", objectId);
                }});
            }});
        }};
        return properties;
    }


    public static void testNested() throws JsonProcessingException {

        MultiValueMap<String, Object> other = new LinkedMultiValueMap<>();
        other.add("objects", dmsProp("xxsAB123"));


        final ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(other);
        System.out.println(json);
    }

    public static void body(final String objectIdValue) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper();

        ObjectNode value = mapper.createObjectNode();
        value.put("value", objectIdValue);

        ObjectNode objectId = mapper.createObjectNode();
        objectId.set("system:objectId", value);

        ObjectNode property = mapper.createObjectNode();
        property.set("properties", objectId);


        ArrayNode propertiesArray = mapper.createArrayNode();
        propertiesArray.add(property);

        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.set("objects", propertiesArray);


        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(json);
    }


    public static void objectNode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

        ObjectNode childNode1 = mapper.createObjectNode();
        childNode1.put("name1", "val1");
        childNode1.put("name2", "val2");

        rootNode.set("obj1", childNode1);

        ObjectNode childNode2 = mapper.createObjectNode();
        childNode2.put("name3", "val3");
        childNode2.put("name4", "val4");

        rootNode.set("obj2", childNode2);

        ObjectNode childNode3 = mapper.createObjectNode();
        childNode3.put("name5", "val5");
        childNode3.put("name6", "val6");

        rootNode.set("obj3", childNode3);


        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        System.out.println(jsonString);
    }


    public static void objectArrayNode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode vendor1 = mapper.createObjectNode();
        vendor1.put("name", "Ford");

        ObjectNode vendor2 = mapper.createObjectNode();
        vendor2.put("name", "BMW");

        ObjectNode vendor3 = mapper.createObjectNode();
        vendor3.put("name", "Fiat");

        // create nested arrays
        ArrayNode models1 = mapper.createArrayNode();
        models1.add("Fiesta");
        models1.add("Focus");
        models1.add("Mustang");

        ArrayNode models2 = mapper.createArrayNode();
        models2.add("320");
        models2.add("X3");
        models2.add("X5");

        ArrayNode models3 = mapper.createArrayNode();
        models3.add("500");
        models3.add("Panda");

        // add nested arrays to JSON objects
        vendor1.set("models", models1);
        vendor2.set("models", models2);
        vendor3.set("models", models3);

        // create `ArrayNode` object
        ArrayNode arrayNode = mapper.createArrayNode();

        // add JSON objects to array
        arrayNode.addAll(Arrays.asList(vendor1, vendor2, vendor3));

        // convert `ArrayNode` to pretty-print JSON
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        System.out.println(json);
    }
}
