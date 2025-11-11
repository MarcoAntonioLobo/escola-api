package com.vlupt.escola_api.controller;

import java.time.Month;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    // Endpoint com filtros simples (sem paginação)
    @GetMapping("/filtered")
    public ResponseEntity<List<MetricsDTO>> getMetricsFiltered(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year) {

        Month monthEnum = (month != null) ? Month.of(month) : null;
        List<MetricsDTO> result = metricsService.getMetricsFiltered(clientId, monthEnum, year);

        return ResponseEntity.ok(result);
    }

    // Endpoint com filtros + paginação usando Spring Data
    @GetMapping("/paged")
    public ResponseEntity<Page<MetricsDTO>> getMetricsPaged(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Month monthEnum = (month != null) ? Month.of(month) : null;
        Pageable pageable = PageRequest.of(page, size);
        Page<MetricsDTO> pagedResult = metricsService.getMetricsFilteredPaged(clientId, monthEnum, year, pageable);

        return ResponseEntity.ok(pagedResult);
    }

    // Endpoint sem filtros (retorna tudo)
    @GetMapping
    public ResponseEntity<List<MetricsDTO>> getAllMetrics() {
        return ResponseEntity.ok(metricsService.calculateAllMetrics());
    }
}
