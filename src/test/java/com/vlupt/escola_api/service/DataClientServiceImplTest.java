package com.vlupt.escola_api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vlupt.escola_api.exception.BadRequestException;
import com.vlupt.escola_api.exception.ConflictException;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.repository.DataClientRepository;
import com.vlupt.escola_api.service.impl.DataClientServiceImpl;

class DataClientServiceImplTest {

    @Mock
    private DataClientRepository repository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private DataClientServiceImpl service;

    private Client client;
    private DataClient dataClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = Client.builder()
                .clientId(1)
                .schoolName("Escola ABC")
                .studentCount(100)
                .build();

        dataClient = DataClient.builder()
                .dataId(1)
                .client(client)
                .monthDate(LocalDate.of(2025, 11, 1))
                .revenue(BigDecimal.valueOf(1000))
                .expenses(BigDecimal.valueOf(500))
                .orderCount(50)
                .registeredStudents(80)
                .notes("Teste")
                .build();
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(Arrays.asList(dataClient));

        List<DataClient> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals(dataClient, result.get(0));
    }

    @Test
    void testFindById_Found() {
        when(repository.findById(1)).thenReturn(Optional.of(dataClient));

        Optional<DataClient> result = service.findById(1);

        assertTrue(result.isPresent());
        assertEquals(dataClient, result.get());
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        Optional<DataClient> result = service.findById(2);

        assertFalse(result.isPresent());
    }

    @Test
    void testSave_Success() {
        when(clientRepository.existsById(client.getClientId())).thenReturn(true);
        when(repository.findByClient_ClientIdAndMonthDate(client.getClientId(), dataClient.getMonthDate()))
                .thenReturn(Optional.empty());
        when(repository.save(dataClient)).thenReturn(dataClient);

        DataClient result = service.save(dataClient);

        assertEquals(dataClient, result);
    }

    @Test
    void testSave_ClientNull() {
        DataClient invalid = DataClient.builder().dataId(2).client(null).build();

        BadRequestException ex = assertThrows(BadRequestException.class, () -> service.save(invalid));
        assertTrue(ex.getMessage().contains("Cliente não pode ser nulo"));
    }

    @Test
    void testSave_ClientNotFound() {
        when(clientRepository.existsById(client.getClientId())).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.save(dataClient));
        assertTrue(ex.getMessage().contains("Cliente não encontrado"));
    }

    @Test
    void testSave_Conflict() {
        when(clientRepository.existsById(client.getClientId())).thenReturn(true);
        when(repository.findByClient_ClientIdAndMonthDate(client.getClientId(), dataClient.getMonthDate()))
                .thenReturn(Optional.of(dataClient));

        ConflictException ex = assertThrows(ConflictException.class, () -> service.save(dataClient));
        assertTrue(ex.getMessage().contains("Já existe registro de dados para esse cliente"));
    }

    @Test
    void testUpdate_Success() {
        DataClient updated = DataClient.builder()
                .client(client)
                .monthDate(dataClient.getMonthDate())
                .revenue(BigDecimal.valueOf(2000))
                .expenses(BigDecimal.valueOf(1000))
                .orderCount(100)
                .registeredStudents(90)
                .notes("Atualizado")
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(dataClient));
        when(clientRepository.existsById(client.getClientId())).thenReturn(true);
        when(repository.findByClient_ClientIdAndMonthDate(client.getClientId(), updated.getMonthDate()))
                .thenReturn(Optional.empty());
        when(repository.save(any(DataClient.class))).thenReturn(updated);

        DataClient result = service.update(1, updated);

        assertEquals(BigDecimal.valueOf(2000), result.getRevenue());
        assertEquals("Atualizado", result.getNotes());
        assertEquals(100, result.getOrderCount());
        assertEquals(90, result.getRegisteredStudents());
    }

    @Test
    void testUpdate_NotFound() {
        when(repository.findById(2)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.update(2, dataClient));
        assertTrue(ex.getMessage().contains("Registro de dados não encontrado"));
    }

    @Test
    void testUpdate_Conflict() {
        Client anotherClient = Client.builder().clientId(2).schoolName("Outra Escola").build();

        DataClient existingOther = DataClient.builder()
                .dataId(2)
                .client(anotherClient)
                .monthDate(dataClient.getMonthDate()) // mesmo mês
                .build();

        DataClient toUpdate = DataClient.builder()
                .client(anotherClient)
                .monthDate(existingOther.getMonthDate()) // mesmo mês do outro registro
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(dataClient));
        when(clientRepository.existsById(anotherClient.getClientId())).thenReturn(true);
        when(repository.findByClient_ClientIdAndMonthDate(anotherClient.getClientId(), toUpdate.getMonthDate()))
                .thenReturn(Optional.of(existingOther));

        ConflictException ex = assertThrows(ConflictException.class, () -> service.update(1, toUpdate));
        assertTrue(ex.getMessage().contains("Já existe registro de dados para esse cliente"));
    }

    @Test
    void testDelete_Success() {
        when(repository.existsById(1)).thenReturn(true);
        doNothing().when(repository).deleteById(1);

        assertDoesNotThrow(() -> service.delete(1));
    }

    @Test
    void testDelete_NotFound() {
        when(repository.existsById(2)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.delete(2));
        assertTrue(ex.getMessage().contains("Registro de dados não encontrado"));
    }

    @Test
    void testFindByClientId() {
        when(repository.findByClient_ClientId(1)).thenReturn(Arrays.asList(dataClient));

        List<DataClient> result = service.findByClientId(1);

        assertEquals(1, result.size());
        assertEquals(dataClient, result.get(0));
    }
}
