package com.vlupt.escola_api.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.dto.MetricsFilterDTO;

public interface MetricsService {

    List<MetricsDTO> calculateAllMetrics();

    List<MetricsDTO> getMetricsFiltered(MetricsFilterDTO filter);

    Page<MetricsDTO> getMetricsFilteredPaged(MetricsFilterDTO filter, Pageable pageable);
}
