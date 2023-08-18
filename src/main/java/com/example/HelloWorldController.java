package com.example;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.*;


@RestController
@RequestMapping("/api")
public class HelloWorldController {
    private static final Logger logger = LoggerFactory.getLogger(HelloWorldController.class);

    @RequestMapping("/hello")
    public String index() {
        System.out.println("0 bla bla");
        System.out.println("1 bla bla");
        System.out.println("2 bla bla");

        return "Hello, World!";
    }

    // @RequestMapping(value = "/objects", method = RequestMethod.POST)
    // public ResponseEntity<> delete(@RequestParam List<String> ids) {
    //      
    // }

    // @RequestMapping(value = "/objects", method = RequestMethod.POST)
    // public ResponseEntity<String> delete(@RequestParam("id") String id) {
    //     return new ResponseEntity<String>(id, HttpStatus.OK);
    // }

    @RequestMapping(value = "/objects", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete(
        @RequestParam(required = false, defaultValue = "true") Boolean greedy,
        @RequestBody List<String> objectIds) {

        logger.info("objectIds >>> : {}", objectIds);
        logger.info("greedy: {}", greedy);

        return new ResponseEntity<String>(greedy.toString(), HttpStatus.OK);
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

}


@Setter
class Person {
}
