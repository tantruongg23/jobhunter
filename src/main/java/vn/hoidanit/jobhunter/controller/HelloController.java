package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;

@RestController
public class HelloController {
    @GetMapping("/")
    public ResponseEntity<RestResponse<String>> getHelloWorld() {

        return ResponseFactory.success("Hello Controller From Spring Boot", "Hello World");
    }

}
