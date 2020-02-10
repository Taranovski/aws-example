package com.taranovski.example.dynamodb.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Alyx on 10.02.2020.
 */
@Controller
@RequestMapping(path = "/healthCheck")
public class HealthCheckController {

    @GetMapping
    public void healthCheck() {
    }
}
