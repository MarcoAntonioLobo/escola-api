package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClientResponseDTO {

    private Integer dataId;
    private Integer clientId;
    private LocalDate monthDate;
    private String location;
    private String school;
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
