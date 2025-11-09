package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataClientResponseDTO {

    private Integer dataId;

    private Integer clientId;

    private LocalDate monthDate;

    private BigDecimal revenue;

    private BigDecimal expenses;

    private Integer orderCount; // NOVO CAMPO

    private String notes;
}
