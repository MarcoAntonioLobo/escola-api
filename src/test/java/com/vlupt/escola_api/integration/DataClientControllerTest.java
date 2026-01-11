package com.vlupt.escola_api.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vlupt.escola_api.controller.DataClientController;
import com.vlupt.escola_api.dto.DataClientRequestDTO;
import com.vlupt.escola_api.dto.DataClientResponseDTO;
import com.vlupt.escola_api.exception.GlobalExceptionHandler;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.mapper.DataClientMapper;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.service.ClientService;
import com.vlupt.escola_api.service.DataClientService;

@ExtendWith(MockitoExtension.class)
class DataClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DataClientService service;

    @Mock
    private ClientService clientService;

    @Mock
    private DataClientMapper mapper;

    @InjectMocks
    private DataClientController controller;

    private ObjectMapper objectMapper;

    private Client client;
    private DataClient dataClient;
    private DataClientRequestDTO requestDTO;
    private DataClientResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        client = Client.builder()
                .clientId(1)
                .schoolName("Escola Teste")
                .build();

        dataClient = DataClient.builder()
                .dataId(1)
                .client(client)
                .monthDate(LocalDate.of(2025, 11, 1))
                .registeredStudents(100)
                .revenue(BigDecimal.valueOf(1000))
                .notes("Teste")
                .build();

        requestDTO = DataClientRequestDTO.builder()
                .clientId(client.getClientId())
                .monthDate(LocalDate.of(2025, 11, 1))
                .location("Lisboa")
                .school("Escola Teste")
                .cafeteria("Cantina Central")
                .registeredStudents(100)
                .revenue(BigDecimal.valueOf(1000))
                .notes("Teste")
                .build();

        responseDTO = DataClientResponseDTO.builder()
                .dataId(1)
                .clientId(client.getClientId())
                .monthDate(requestDTO.getMonthDate())
                .location(requestDTO.getLocation())
                .school(requestDTO.getSchool())
                .cafeteria(requestDTO.getCafeteria())
                .registeredStudents(requestDTO.getRegisteredStudents())
                .revenue(requestDTO.getRevenue())
                .notes(requestDTO.getNotes())
                .build();
    }

    @Test
    void testCreate() throws Exception {
        when(clientService.findById(client.getClientId()))
                .thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client))
                .thenReturn(dataClient);
        when(service.save(dataClient))
                .thenReturn(dataClient);
        when(mapper.toResponse(dataClient))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/client-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataId").value(1))
                .andExpect(jsonPath("$.school").value("Escola Teste"));
    }

    @Test
    void testCreate_ClientNotFound() throws Exception {
        when(clientService.findById(client.getClientId()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/client-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testUpdate() throws Exception {
        when(clientService.findById(client.getClientId()))
                .thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client))
                .thenReturn(dataClient);
        when(service.update(1, dataClient))
                .thenReturn(dataClient);
        when(mapper.toResponse(dataClient))
                .thenReturn(responseDTO);

        mockMvc.perform(put("/api/client-data/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.school").value("Escola Teste"));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(clientService.findById(client.getClientId()))
                .thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client))
                .thenReturn(dataClient);
        when(service.update(1, dataClient))
                .thenThrow(new ResourceNotFoundException("Registro não encontrado"));

        mockMvc.perform(put("/api/client-data/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
