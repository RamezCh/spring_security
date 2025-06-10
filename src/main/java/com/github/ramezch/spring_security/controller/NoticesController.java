package com.github.ramezch.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    @GetMapping("/notices")
    public String getNoticesDetails() {
        return "Here are the notices details from the DB";
    }
}
