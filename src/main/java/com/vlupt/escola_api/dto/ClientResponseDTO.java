package com.vlupt.escola_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientResponseDTO {

    @Schema(description = "ID interno do cliente", example = "1")
    private Integer clientId;

    @Schema(description = "ID externo do cliente", example = "EXT123")
    private String externalId;

    @Schema(description = "Nome da escola", example = "Escola ABC")
    private String schoolName;

    @Schema(description = "Nome da cantina", example = "Cantina ABC")
    private String cafeteriaName;

    @Schema(description = "Localização da escola", example = "Rua das Flores, 123")
    private String location;

    @Schema(description = "Quantidade de alunos", example = "250")
    private Integer studentCount;
}
