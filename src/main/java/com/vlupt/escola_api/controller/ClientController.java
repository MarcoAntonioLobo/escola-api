package com.vlupt.escola_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vlupt.escola_api.dto.ClientRequestDTO;
import com.vlupt.escola_api.dto.ClientResponseDTO;
import com.vlupt.escola_api.dto.ErrorResponse;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.mapper.ClientMapper;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    public ClientController(ClientService service, ClientMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Operation(summary = "Lista todos os clientes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<ClientResponseDTO> findAll() {
        return service.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Operation(summary = "Busca cliente por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Integer id) {
        Client client = service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ResponseEntity.ok(mapper.toResponse(client));
    }

    @Operation(summary = "Cria um novo cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente criado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto) {
        Client saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.status(201).body(mapper.toResponse(saved));
    }

    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClientRequestDTO dto) {
        Client updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @Operation(summary = "Deleta um cliente")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente deletado"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno",
                     content = @io.swagger.v3.oas.annotations.media.Content(
                         schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
