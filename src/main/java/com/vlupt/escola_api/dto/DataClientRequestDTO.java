package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClientRequestDTO {
    private Integer clientId;
    private LocalDate monthDate;
    private BigDecimal revenue;
    private BigDecimal expenses;
    private Integer orderCount;
    private String notes;
}
