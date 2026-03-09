package com.Prasad.Jobsy2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/status"})
public class HelloController {
    @GetMapping
    public String statusCheck(){
        return "Application is running";
    }
}
