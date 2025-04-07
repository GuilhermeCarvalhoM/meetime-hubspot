package com.HubConnect.meetime.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.HubConnect.meetime.controller.dto.ContactRequest;

import java.util.*;

@Service
public class HubspotService {
	@Autowired
	private TokenService tokenService;
    @Value("${hubspot.access-token}")
    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<String> createContact(ContactRequest contact) {
    	String token = tokenService.getToken();
        String accessToken =token;
        if (accessToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de acesso não disponível.");
        }

        String url = "https://api.hubapi.com/crm/v3/objects/contacts";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        String json = String.format("""
        {
          "properties": {
            "email": "%s",
            "firstname": "%s",
            "lastname": "%s"
          }
        }
        """, contact.getEmail(), contact.getFirstName(), contact.getLastName());

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar contato: " + e.getMessage());
        }
    }

}