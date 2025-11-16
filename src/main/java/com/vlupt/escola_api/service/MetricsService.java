package com.vlupt.escola_api.service;

import java.time.Month;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vlupt.escola_api.dto.MetricsDTO;

public interface MetricsService {

    List<MetricsDTO> calculateAllMetrics();

	List<MetricsDTO> getMetricsFiltered(Long clientId, Month month, Integer year);

	Page<MetricsDTO> getMetricsFilteredPaged(Long clientId, Month monthEnum, Integer year, Pageable pageable);
}