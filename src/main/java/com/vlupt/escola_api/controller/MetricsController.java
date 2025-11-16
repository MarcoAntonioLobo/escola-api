package com.vlupt.escola_api.controller;

import java.time.Month;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.service.MetricsService;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping
    public ResponseEntity<List<MetricsDTO>> getMetrics(
        @RequestParam(required = false) Long clientId,
        @RequestParam(required = false) Month month,
        @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(
            metricsService.getMetricsFiltered(clientId, month, year)
        );
    }

    @GetMapping("/paged")
    public Page<MetricsDTO> getMetricsPaged(
        @RequestParam(required = false) Long clientId,
        @RequestParam(required = false) Month month,
        @RequestParam(required = false) Integer year,
        Pageable pageable
    ) {
        return metricsService.getMetricsFilteredPaged(clientId, month, year, pageable);
    }
}
