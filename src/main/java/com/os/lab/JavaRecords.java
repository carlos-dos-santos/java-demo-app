package com.os.lab;

import java.util.Date;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

record Person(
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    String address,
    Date birthday,
    List<String> achievements) {
}

// record DmsObject (
//    Map<String, DmsObjectProperty> properties = new HashSet<>();
// })

public class JavaRecords {
   public static void main(String... args) throws JsonProcessingException {
      var person = new Person("John", "Doe", "USA", new Date(981291289182L), List.of("Speaker"));

      final ObjectMapper mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);

      var serialized = mapper.writeValueAsString(person);
      System.out.println(serialized);

      MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
      //requestBody.add("objects", );
      var rb = mapper.writeValueAsString(requestBody);
      System.out.println(rb);
   }
}
