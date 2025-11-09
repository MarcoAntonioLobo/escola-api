package com.vlupt.escola_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.service.MetricsService;

@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public ResponseEntity<List<MetricsDTO>> getAllMetrics() {
        return ResponseEntity.ok(metricsService.calculateAllMetrics());
    }
}
