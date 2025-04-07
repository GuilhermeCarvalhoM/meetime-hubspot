package com.HubConnect.meetime.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/contact")
    public ResponseEntity<Void> handleContactWebhook(@RequestBody List<Map<String, Object>> events) {
        logger.info("📩 Webhook recebido: {}", events);

        for (Map<String, Object> event : events) {
            logger.info("🔔 Evento recebido: {}", event);
        }

        return ResponseEntity.ok().build();
    }
}
