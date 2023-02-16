package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @GetMapping(value = "hello")
    //@PreAuthorize("hasAnyAuthority('admin')")
    public String hello(){
        return "Hello World";
    }

    @GetMapping(value = "hello2")
    //@PreAuthorize("hasAnyAuthority('default-roles-master')")
    public String hello2(){
        return "Hello World 2";
    }
}
