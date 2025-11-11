package com.vlupt.escola_api.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vlupt.escola_api.controller.ClientController;
import com.vlupt.escola_api.dto.ClientRequestDTO;
import com.vlupt.escola_api.exception.GlobalExceptionHandler;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.mapper.ClientMapper;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.service.ClientService;

class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientService service;

    private ClientMapper mapper;

    @InjectMocks
    private ClientController controller;

    private ObjectMapper objectMapper;

    private ClientRequestDTO requestDTO;
    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        mapper = new ClientMapper();
        controller = new ClientController(service, mapper);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        requestDTO = new ClientRequestDTO();
        requestDTO.setExternalId("EXT123");
        requestDTO.setSchoolName("Escola ABC");
        requestDTO.setCafeteriaName("Cantina ABC");
        requestDTO.setLocation("Rua das Flores, 123");
        requestDTO.setStudentCount(250);

        client = mapper.toEntity(requestDTO);
        client.setClientId(1);
    }

    @Test
    void testCreate_Valid() throws Exception {
        when(service.save(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.schoolName").value("Escola ABC"));
    }

    @Test
    void testCreate_InvalidJson() throws Exception {
        ClientRequestDTO invalidDTO = new ClientRequestDTO();
        invalidDTO.setExternalId("");
        invalidDTO.setSchoolName("");
        invalidDTO.setCafeteriaName("");
        invalidDTO.setLocation("");
        invalidDTO.setStudentCount(null);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testUpdate_Success() throws Exception {
        when(service.update(eq(1), any(Client.class))).thenReturn(client);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.schoolName").value("Escola ABC"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Cliente não encontrado"))
                .when(service).update(eq(1), any(Client.class));

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"));
    }

    @Test
    void testFindAll_Empty() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testFindById_Found() throws Exception {
        when(service.findById(1)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1))
                .andExpect(jsonPath("$.schoolName").value("Escola ABC"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(service.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"));
    }

    @Test
    void testDelete_Success() throws Exception {
        doNothing().when(service).delete(1);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Cliente não encontrado"))
                .when(service).delete(1);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"));
    }
}
