package com.vlupt.escola_api.mapper;

import org.springframework.stereotype.Component;

import com.vlupt.escola_api.dto.DataClientRequestDTO;
import com.vlupt.escola_api.dto.DataClientResponseDTO;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;

@Component
public class DataClientMapper {

    public DataClient toEntity(DataClientRequestDTO dto, Client client) {
        if (dto == null || client == null) {
            return null;
        }

        return DataClient.builder()
                .client(client)
                .monthDate(dto.getMonthDate())
                .revenue(dto.getRevenue())
                .expenses(dto.getExpenses())
                .orderCount(dto.getOrderCount())
                .notes(dto.getNotes())
                .build();
    }

    public DataClientResponseDTO toResponse(DataClient entity) {
        if (entity == null) {
            return null;
        }

        return DataClientResponseDTO.builder()
                .dataId(entity.getDataId())
                .clientId(entity.getClient().getClientId())
                .monthDate(entity.getMonthDate())
                .revenue(entity.getRevenue())
                .expenses(entity.getExpenses())
                .orderCount(entity.getOrderCount())
                .registeredStudents(entity.getRegisteredStudents())
                .notes(entity.getNotes())
                .build();
    }
}
