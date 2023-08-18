package com.os.lab;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

// http://www.java2s.com/example/java-api/org/springframework/http/httpentity/httpentity-2-0.html

public class HttpEntityLab {
    public static void main(String... args) {
        testStringEntity();
    }

    public static void testStringEntity() {
        RestTemplate tmp = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer d724997c-ba1d-42aa-8ed3-40a8a590558e");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // try {
        //     ResponseEntity<domain.Input_presenteDTO[]> input =
        //     tmp.exchange("http://localhost:9191/test"
        //         + assunto,
        //         HttpMethod.GET, entity, Input_presenteDTO[].class);
        //     System.out.println("Fez o download do assunto: " + assunto);
        //     System.out.println("Tamano input: " + input.getBody().length + "  Assunto: " + assunto);
        // } catch (Exception e) {
        //     System.out.println(e);
        // }
    }

    public static void testMultiValueMap(String[] args) {
        // RestTemplate template = new RestTemplate();
        // String uri = "http://localhost:8080/http-adapter-1.0.0/inboundMultipartAdapter.html";
        //
        // Resource picture = new ClassPathResource("com/apress/prospringintegration/web/test.png");
        // MultiValueMap<String, String> map = new LinkedMultiValueMap<>(); // Map<String, List<String>>
        //
        // map.add("name", "John Smith");
        // map.add("picture", picture);
        //
        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(new MediaType("multipart", "form-data"));
        //
        // HttpEntity request = new HttpEntity(map, headers);
        // ResponseEntity<?> httpResponse = template.exchange(uri, HttpMethod.POST, request, null);
        //
        // System.out.println("Status: " + httpResponse.getStatusCode().name());
    }
}
