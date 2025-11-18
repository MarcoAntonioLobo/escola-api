package com.vlupt.escola_api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.dto.MetricsFilterDTO;
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
        @RequestParam(required = false) String schoolName,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String direction
    ) {
        MetricsFilterDTO filter = new MetricsFilterDTO();
        filter.setSchoolName(schoolName);
        filter.setMonth(month != null ? java.time.Month.of(month) : null);
        filter.setYear(year);
        filter.setSortBy(sortBy);
        filter.setSortDirection(direction);

        return ResponseEntity.ok(metricsService.getMetricsFiltered(filter));
    }

    @GetMapping("/paged")
    public Page<MetricsDTO> getMetricsPaged(
        @RequestParam(required = false) String schoolName,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String direction,
        Pageable pageable
    ) {
        MetricsFilterDTO filter = new MetricsFilterDTO();
        filter.setSchoolName(schoolName);
        filter.setMonth(month != null ? java.time.Month.of(month) : null);
        filter.setYear(year);
        filter.setSortBy(sortBy);
        filter.setSortDirection(direction);

        return metricsService.getMetricsFilteredPaged(filter, pageable);
    }
}
