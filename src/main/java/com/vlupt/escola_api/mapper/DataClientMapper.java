package com.vlupt.escola_api.mapper;

import org.springframework.stereotype.Component;
import com.vlupt.escola_api.dto.DataClientRequestDTO;
import com.vlupt.escola_api.dto.DataClientResponseDTO;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;

@Component
public class DataClientMapper {

    public DataClient toEntity(DataClientRequestDTO dto, Client client) {
        if (dto == null || client == null) return null;

        return DataClient.builder()
                .client(client)
                .monthDate(dto.getMonthDate())
                .cantinaPercent(dto.getCantinaPercent())
                .registeredStudents(dto.getRegisteredStudents())
                .averageCantinaPerStudent(dto.getAverageCantinaPerStudent())
                .averagePedagogicalPerStudent(dto.getAveragePedagogicalPerStudent())
                .orderCount(dto.getOrderCount())
                .revenue(dto.getRevenue())
                .profitability(dto.getProfitability())
                .revenueLoss(dto.getRevenueLoss())
                .ordersOutsideVpt(dto.getOrdersOutsideVpt())
                .averageTicketApp(dto.getAverageTicketApp())
                .notes(dto.getNotes())
                .build();
    }

    public DataClientResponseDTO toResponse(DataClient entity) {
        if (entity == null) return null;

        Client client = entity.getClient();

        return DataClientResponseDTO.builder()
                .dataId(entity.getDataId())
                .clientId(client.getClientId())
                .monthDate(entity.getMonthDate())
                .location(client.getLocation())
                .school(client.getSchoolName())
                .cafeteria(client.getCafeteriaName())
                .cantinaPercent(entity.getCantinaPercent())
                .registeredStudents(entity.getRegisteredStudents())
                .averageCantinaPerStudent(entity.getAverageCantinaPerStudent())
                .averagePedagogicalPerStudent(entity.getAveragePedagogicalPerStudent())
                .orderCount(entity.getOrderCount())
                .revenue(entity.getRevenue())
                .profitability(entity.getProfitability())
                .revenueLoss(entity.getRevenueLoss())
                .ordersOutsideVpt(entity.getOrdersOutsideVpt())
                .averageTicketApp(entity.getAverageTicketApp())
                .notes(entity.getNotes())
                .build();
    }
}
