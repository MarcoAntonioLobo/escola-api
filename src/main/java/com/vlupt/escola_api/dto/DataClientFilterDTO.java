package com.vlupt.escola_api.dto;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClientFilterDTO {

    private Integer clientId; // Filtra por cliente
    private LocalDate dateStart; // Data inicial
    private LocalDate dateEnd;   // Data final
    private String sortBy;       // Campo de ordenação (ex: "monthDate", "revenue", "expenses")
    private String direction;    // "asc" ou "desc"
}
