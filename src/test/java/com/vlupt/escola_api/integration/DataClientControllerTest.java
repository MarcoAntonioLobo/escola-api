package com.vlupt.escola_api.integration;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

        client = Client.builder().clientId(1).schoolName("Escola Teste").build();

        dataClient = DataClient.builder()
                .dataId(1)
                .client(client)
                .monthDate(LocalDate.of(2025, 11, 1))
                .revenue(BigDecimal.valueOf(1000))
                .expenses(BigDecimal.valueOf(500))
                .orderCount(10)
                .registeredStudents(100)
                .notes("Teste")
                .build();

        requestDTO = new DataClientRequestDTO();
        requestDTO.setClientId(client.getClientId());
        requestDTO.setMonthDate(dataClient.getMonthDate());
        requestDTO.setRevenue(dataClient.getRevenue());
        requestDTO.setExpenses(dataClient.getExpenses());
        requestDTO.setOrderCount(dataClient.getOrderCount());
        requestDTO.setRegisteredStudents(dataClient.getRegisteredStudents());
        requestDTO.setNotes(dataClient.getNotes());

        responseDTO = DataClientResponseDTO.builder()
                .dataId(dataClient.getDataId())
                .clientId(client.getClientId())
                .monthDate(dataClient.getMonthDate())
                .revenue(dataClient.getRevenue())
                .expenses(dataClient.getExpenses())
                .orderCount(dataClient.getOrderCount())
                .registeredStudents(dataClient.getRegisteredStudents())
                .notes(dataClient.getNotes())
                .build();
    }

    @Test
    void testFindAll() throws Exception {
        when(service.findAll()).thenReturn(List.of(dataClient));
        when(mapper.toResponse(dataClient)).thenReturn(responseDTO);

        mockMvc.perform(get("/client-data"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dataId").value(dataClient.getDataId()))
                .andExpect(jsonPath("$[0].orderCount").value(dataClient.getOrderCount()))
                .andExpect(jsonPath("$[0].registeredStudents").value(dataClient.getRegisteredStudents()));
    }

    @Test
    void testFindById() throws Exception {
        when(service.findById(1)).thenReturn(Optional.of(dataClient));
        when(mapper.toResponse(dataClient)).thenReturn(responseDTO);

        mockMvc.perform(get("/client-data/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataId").value(dataClient.getDataId()))
                .andExpect(jsonPath("$.orderCount").value(dataClient.getOrderCount()))
                .andExpect(jsonPath("$.registeredStudents").value(dataClient.getRegisteredStudents()));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(service.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/client-data/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testCreate() throws Exception {
        when(clientService.findById(client.getClientId())).thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client)).thenReturn(dataClient);
        when(service.save(dataClient)).thenReturn(dataClient);
        when(mapper.toResponse(dataClient)).thenReturn(responseDTO);

        mockMvc.perform(post("/client-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dataId").value(dataClient.getDataId()))
                .andExpect(jsonPath("$.orderCount").value(dataClient.getOrderCount()))
                .andExpect(jsonPath("$.registeredStudents").value(dataClient.getRegisteredStudents()));
    }

    @Test
    void testCreate_ClientNotFound() throws Exception {
        when(clientService.findById(client.getClientId())).thenReturn(Optional.empty());

        mockMvc.perform(post("/client-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testUpdate() throws Exception {
        when(clientService.findById(client.getClientId())).thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client)).thenReturn(dataClient);
        when(service.update(1, dataClient)).thenReturn(dataClient);
        when(mapper.toResponse(dataClient)).thenReturn(responseDTO);

        mockMvc.perform(put("/client-data/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataId").value(dataClient.getDataId()))
                .andExpect(jsonPath("$.orderCount").value(dataClient.getOrderCount()))
                .andExpect(jsonPath("$.registeredStudents").value(dataClient.getRegisteredStudents()));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        when(clientService.findById(client.getClientId())).thenReturn(Optional.of(client));
        when(mapper.toEntity(requestDTO, client)).thenReturn(dataClient);
        when(service.update(1, dataClient)).thenThrow(new ResourceNotFoundException("Registro não encontrado"));

        mockMvc.perform(put("/client-data/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/client-data/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Registro não encontrado")).when(service).delete(1);

        mockMvc.perform(delete("/client-data/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void testFindByClient() throws Exception {
        when(service.findByClientId(client.getClientId())).thenReturn(List.of(dataClient));
        when(mapper.toResponse(dataClient)).thenReturn(responseDTO);

        mockMvc.perform(get("/client-data/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dataId").value(dataClient.getDataId()))
                .andExpect(jsonPath("$[0].orderCount").value(dataClient.getOrderCount()))
                .andExpect(jsonPath("$[0].registeredStudents").value(dataClient.getRegisteredStudents()));
    }
}
