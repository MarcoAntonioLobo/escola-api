package com.vlupt.escola_api.mapper;

import java.math.BigDecimal;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.model.DataClient;

public class MetricsMapper {

    public static MetricsDTO toDTO(
            String schoolName,
            Integer totalStudentsSchool,
            Integer totalStudentsRegistered,
            BigDecimal profitPerStudent,
            BigDecimal averageTicket,
            Double averageOrdersPerStudent,
            Integer totalOrdersMonth,
            DataClient dataClient
    ) {
        return MetricsDTO.builder()
                .schoolName(schoolName)
                .totalStudentsSchool(totalStudentsSchool)
                .totalStudentsRegistered(totalStudentsRegistered)
                .profitPerStudent(profitPerStudent)
                .averageTicket(averageTicket)
                .averageOrdersPerStudent(averageOrdersPerStudent)
                .totalOrdersMonth(totalOrdersMonth)
                .month(dataClient.getMonthDate().getMonth())
                .build();
    }
}