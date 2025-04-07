package com.HubConnect.meetime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.HubConnect.meetime.controller.dto.ContactRequest;
import com.HubConnect.meetime.service.HubspotService;
import com.HubConnect.meetime.util.RateLimiter;

import java.util.Map;

@RestController
public class ContactController {

    @Autowired
    private HubspotService hubspotService;

    @Autowired
    private RateLimiter rateLimiter;

    @PostMapping("/contacts")
    public ResponseEntity<String> createContact(@RequestBody ContactRequest contactRequest) {
        if (!rateLimiter.allowRequest()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate limit excedido");
        }

        return hubspotService.createContact(contactRequest);
    }
}