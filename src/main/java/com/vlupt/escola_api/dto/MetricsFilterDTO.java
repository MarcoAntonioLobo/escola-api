package com.vlupt.escola_api.dto;

import java.time.Month;
import lombok.Data;

@Data
public class MetricsFilterDTO {
    private String schoolName; // substituído de clientId
    private Month month;
    private Integer year;
    private String sortBy;
    private String sortDirection;
}
