package com.example.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/Demo")
public class DemoController {

    @GetMapping("uno")
    public ResponseEntity<String> sayhello(){
        return ResponseEntity.ok("Hello i am a secure endpoint");
    }

    @GetMapping("dos")
    public ResponseEntity<String> sayhello2(){
        return ResponseEntity.ok("Hello soy el dos estoy vivo");
    }

}
