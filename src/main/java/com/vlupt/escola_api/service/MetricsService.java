package com.vlupt.escola_api.service;

import java.util.List;

import com.vlupt.escola_api.dto.MetricsDTO;

public interface MetricsService {

    List<MetricsDTO> calculateAllMetrics();
}
