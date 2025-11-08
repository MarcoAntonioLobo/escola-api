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

import com.vlupt.escola_api.dto.DataClientRequestDTO;
import com.vlupt.escola_api.dto.DataClientResponseDTO;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.mapper.DataClientMapper;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.service.ClientService;
import com.vlupt.escola_api.service.DataClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client-data")
public class DataClientController {

    private final DataClientService service;
    private final DataClientMapper mapper;
    private final ClientService clientService;

    public DataClientController(DataClientService service,
                                DataClientMapper mapper,
                                ClientService clientService) {
        this.service = service;
        this.mapper = mapper;
        this.clientService = clientService;
    }

    @GetMapping
    public List<DataClientResponseDTO> findAll() {
        return service.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataClientResponseDTO> findById(@PathVariable Integer id) {
        DataClient data = service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registro não encontrado"));
        return ResponseEntity.ok(mapper.toResponse(data));
    }

    @PostMapping
    public ResponseEntity<DataClientResponseDTO> create(@Valid @RequestBody DataClientRequestDTO dto) {
        var client = clientService.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        DataClient saved = service.save(mapper.toEntity(dto, client));
        return ResponseEntity.status(201).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataClientResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DataClientRequestDTO dto) {

        var client = clientService.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        DataClient updated = service.update(id, mapper.toEntity(dto, client));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public List<DataClientResponseDTO> findByClient(@PathVariable Integer clientId) {
        return service.findByClientId(clientId).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
