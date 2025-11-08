package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DataClientRequestDTO {

    @Schema(description = "ID do cliente", example = "1")
    private Integer clientId;

    @Schema(description = "Mês do registro (YYYY-MM-DD)", example = "2025-11-01")
    private LocalDate monthDate;

    @Schema(description = "Faturamento do mês", example = "12500.50")
    private BigDecimal revenue;

    @Schema(description = "Despesas do mês", example = "3500.75")
    private BigDecimal expenses;

    @Schema(description = "Observações adicionais", example = "Mes com aumento de alunos")
    private String notes;
}
