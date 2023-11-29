package edu.hillel.newsapiproxy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Ping {

    @RequestMapping("/ping")
    public String ping() {
        return "OK";
    }
}
