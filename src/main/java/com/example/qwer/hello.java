package com.example.qwer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class hello {
    @GetMapping("/")
    public ResponseEntity<String> hello() {
        String msg ="hello";
        return ResponseEntity.ok(msg);
    }
}