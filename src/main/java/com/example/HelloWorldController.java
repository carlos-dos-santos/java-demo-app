package com.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.*;

@RestController
public class HelloWorldController {
    @RequestMapping("/")
    String index() {
        System.out.println("0 bla bla");
        System.out.println("1 bla bla");
        System.out.println("2 bla bla");
        return "Hello, World!";
    }
}

@Setter
class Person {
}
