package com.HubConnect.meetime.controller;

import com.HubConnect.meetime.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.client-secret}")
    private String clientSecret;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private TokenService tokenService;

    @GetMapping("/url")
    public ResponseEntity<String> getAuthUrl() {
        String scopes = "crm.objects.contacts.read crm.objects.contacts.write oauth";
        String url = "https://app.hubspot.com/oauth/authorize"
                + "?client_id=" + clientId
                + "&scope=" + URLEncoder.encode(scopes, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);

        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String tokenUrl = "https://api.hubapi.com/oauth/v1/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
            Map<String, Object> tokenResponse = response.getBody();

            if (tokenResponse != null && tokenResponse.containsKey("access_token")) {
                tokenService.saveToken((String) tokenResponse.get("access_token"));
            }

            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao trocar código por token: " + e.getMessage());
        }
    }
}
