package com.vlupt.escola_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Resposta de erro padrão")
public class ErrorResponse {

    @Schema(example = "2025-11-08T12:00:00")
    private String timestamp;

    @Schema(example = "404")
    private int status;

    @Schema(example = "Not Found")
    private String error;

    @Schema(example = "Cliente não encontrado")
    private String message;
}
