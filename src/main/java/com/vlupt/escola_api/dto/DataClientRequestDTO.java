package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClientRequestDTO {

    @NotNull
    private Integer clientId;

    @NotNull
    private LocalDate monthDate;
    
    @NotNull
    private String location;
    
    @NotNull
    private String school;
    
    @NotNull
    private String cafeteria;


    // Cantina
    private BigDecimal cantinaPercent;
    private Integer registeredStudents;
    private BigDecimal averageCantinaPerStudent;
    private BigDecimal averagePedagogicalPerStudent;
    private Integer orderCount;
    private BigDecimal revenue;

    // Vlupt
    private BigDecimal profitability;
    private BigDecimal revenueLoss;
    private Integer ordersOutsideVpt;
    private BigDecimal averageTicketApp;

    private String notes;
}
