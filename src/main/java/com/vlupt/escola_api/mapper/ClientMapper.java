package com.vlupt.escola_api.mapper;

import org.springframework.stereotype.Component;

import com.vlupt.escola_api.dto.ClientRequestDTO;
import com.vlupt.escola_api.dto.ClientResponseDTO;
import com.vlupt.escola_api.model.Client;

@Component
public class ClientMapper {

    public Client toEntity(ClientRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return Client.builder()
                .externalId(dto.getExternalId())
                .schoolName(dto.getSchoolName())
                .cafeteriaName(dto.getCafeteriaName())
                .location(dto.getLocation())
                .studentCount(dto.getStudentCount())
                .build();
    }

    public ClientResponseDTO toResponse(Client entity) {
        if (entity == null) {
            return null;
        }

        return ClientResponseDTO.builder()
                .clientId(entity.getClientId())
                .externalId(entity.getExternalId())
                .schoolName(entity.getSchoolName())
                .cafeteriaName(entity.getCafeteriaName())
                .location(entity.getLocation())
                .studentCount(entity.getStudentCount())
                .build();
    }
}
