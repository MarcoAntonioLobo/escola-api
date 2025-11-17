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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vlupt.escola_api.dto.ClientFilterDTO;
import com.vlupt.escola_api.dto.ClientRequestDTO;
import com.vlupt.escola_api.dto.ClientResponseDTO;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.mapper.ClientMapper;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.service.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;
    private final ClientMapper mapper;

    public ClientController(ClientService service, ClientMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ClientResponseDTO> findAll() {
        return service.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> findById(@PathVariable Integer id) {
        Client client = service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        return ResponseEntity.ok(mapper.toResponse(client));
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientRequestDTO dto) {
        Client saved = service.save(mapper.toEntity(dto));
        return ResponseEntity.status(201).body(mapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClientRequestDTO dto) {
        Client updated = service.update(id, mapper.toEntity(dto));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =============================================
    // POST /filter  (BODY)
    // =============================================
    @PostMapping("/filter")
    public List<ClientResponseDTO> filterClients(@RequestBody ClientFilterDTO filter) {
        return service.filter(filter).stream()
                .map(mapper::toResponse)
                .toList();
    }

    // =============================================
    // GET /filter  (QUERY PARAMS)
    // =============================================
    @GetMapping("/filter")
    public List<ClientResponseDTO> filterClientsGet(
            @RequestParam(required = false) Integer clientId,
            @RequestParam(required = false) String schoolName,
            @RequestParam(required = false) String externalId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String cafeteriaName,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDirection
    ) {

        ClientFilterDTO filter = new ClientFilterDTO();
        filter.setClientId(clientId);
        filter.setSchoolName(schoolName);
        filter.setExternalId(externalId);
        filter.setLocation(location);
        filter.setCafeteriaName(cafeteriaName);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        return service.filter(filter)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
