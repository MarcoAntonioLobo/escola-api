package com.vlupt.escola_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientRequestDTO {

    @Schema(description = "ID externo do cliente", example = "EXT123")
    @NotBlank(message = "External ID não pode ser vazio")
    private String externalId;

    @Schema(description = "Nome da escola", example = "Escola ABC")
    @NotBlank(message = "Nome da escola é obrigatório")
    private String schoolName;

    @Schema(description = "Nome da cantina", example = "Cantina ABC")
    @NotBlank(message = "Nome da cantina é obrigatório")
    private String cafeteriaName;

    @Schema(description = "Localização da escola", example = "Rua das Flores, 123")
    @NotBlank(message = "Localização é obrigatória")
    private String location;

    @Schema(description = "Quantidade de alunos", example = "250")
    @NotNull(message = "Quantidade de alunos é obrigatória")
    private Integer studentCount;
}
