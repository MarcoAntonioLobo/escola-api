package com.vlupt.escola_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vlupt.escola_api.exception.ConflictException;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.repository.DataClientRepository;
import com.vlupt.escola_api.service.impl.ClientServiceImpl;
import com.vlupt.escola_api.service.impl.DataClientServiceImpl;

@ExtendWith(MockitoExtension.class)
class ServiceIntegrationTestWithMocks {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DataClientRepository dataClientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @InjectMocks
    private DataClientServiceImpl dataClientService;

    private Client client;
    private DataClient dataClient;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientId(1)
                .externalId("EXT123")
                .schoolName("Escola ABC")
                .cafeteriaName("Cantina ABC")
                .location("Rua das Flores, 123")
                .studentCount(250)
                .build();

        dataClient = DataClient.builder()
                .dataId(1)
                .client(client)
                .monthDate(LocalDate.of(2025, 11, 1))
                .revenue(BigDecimal.valueOf(12000))
                .orderCount(50)
                .registeredStudents(200)
                .notes("Teste inicial")
                .build();
    }

    @Test
    void testClientAndDataClientFlow() {

        when(clientRepository.save(client)).thenReturn(client);
        Client savedClient = clientService.save(client);
        assertNotNull(savedClient);
        assertEquals(client.getClientId(), savedClient.getClientId());

        when(clientRepository.existsById(client.getClientId())).thenReturn(true);
        when(dataClientRepository.findByClient_ClientIdAndMonthDate(client.getClientId(), dataClient.getMonthDate()))
                .thenReturn(Optional.empty());
        when(dataClientRepository.save(dataClient)).thenReturn(dataClient);

        DataClient savedData = dataClientService.save(dataClient);
        assertNotNull(savedData);
        assertEquals(client.getClientId(), savedData.getClient().getClientId());
        assertEquals(200, savedData.getRegisteredStudents());
        assertEquals(50, savedData.getOrderCount());

        when(dataClientRepository.findByClient_ClientIdAndMonthDate(client.getClientId(), dataClient.getMonthDate()))
                .thenReturn(Optional.of(dataClient));
        ConflictException exception = assertThrows(ConflictException.class, () -> dataClientService.save(dataClient));
        assertTrue(exception.getMessage().contains("Já existe registro para este cliente neste mês"));

        DataClient updatedData = DataClient.builder()
                .client(client)
                .monthDate(dataClient.getMonthDate())
                .revenue(BigDecimal.valueOf(15000))
                .orderCount(60)
                .registeredStudents(220)
                .notes("Atualizado")
                .build();

        when(dataClientRepository.findById(1)).thenReturn(Optional.of(dataClient));
        when(dataClientRepository.findByClient_ClientIdAndMonthDate(client.getClientId(), updatedData.getMonthDate()))
                .thenReturn(Optional.empty());
        when(dataClientRepository.save(any(DataClient.class))).thenReturn(updatedData);

        DataClient resultUpdate = dataClientService.update(1, updatedData);
        assertEquals(BigDecimal.valueOf(15000), resultUpdate.getRevenue());
        assertEquals("Atualizado", resultUpdate.getNotes());
        assertEquals(220, resultUpdate.getRegisteredStudents());
        assertEquals(60, resultUpdate.getOrderCount());

        when(dataClientRepository.existsById(1)).thenReturn(true);
        doNothing().when(dataClientRepository).deleteById(1);
        dataClientService.delete(1);
        verify(dataClientRepository, times(1)).deleteById(1);

        when(clientRepository.existsById(client.getClientId())).thenReturn(true);
        doNothing().when(clientRepository).deleteById(client.getClientId());
        clientService.delete(client.getClientId());
        verify(clientRepository, times(1)).deleteById(client.getClientId());
    }

    @Test
    void testUpdateDataClientNotFound() {
        when(dataClientRepository.findById(2)).thenReturn(Optional.empty());
        DataClient update = DataClient.builder().build();
        assertThrows(ResourceNotFoundException.class, () -> dataClientService.update(2, update));
    }

    @Test
    void testDeleteDataClientNotFound() {
        when(dataClientRepository.existsById(2)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> dataClientService.delete(2));
    }
}
