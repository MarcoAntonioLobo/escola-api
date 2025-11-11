package com.vlupt.escola_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.service.impl.ClientServiceImpl;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = Client.builder()
                .clientId(1)
                .externalId("EXT123")
                .schoolName("Escola ABC")
                .cafeteriaName("Cantina ABC")
                .location("Rua X, 123")
                .studentCount(100)
                .build();
    }

    @Test
    void testFindAll() {
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));

        List<Client> result = clientService.findAll();

        assertEquals(1, result.size());
        assertEquals("Escola ABC", result.get(0).getSchoolName());
    }

    @Test
    void testFindById_Success() {
        when(clientRepository.findById(1)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.findById(1);

        assertTrue(result.isPresent());
        assertEquals("Escola ABC", result.get().getSchoolName());
    }

    @Test
    void testFindById_NotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.findById(1);

        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        when(clientRepository.save(client)).thenReturn(client);

        Client saved = clientService.save(client);

        assertEquals("Escola ABC", saved.getSchoolName());
        assertEquals("EXT123", saved.getExternalId());
        assertEquals(100, saved.getStudentCount());
    }

    @Test
    void testUpdate_Success() {
        Client updatedData = Client.builder()
                .externalId("EXT999")
                .schoolName("Escola XYZ")
                .cafeteriaName("Cantina XYZ")
                .location("Rua Y, 456")
                .studentCount(120)
                .build();

        when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        Client updated = clientService.update(1, updatedData);

        assertEquals("Escola XYZ", updated.getSchoolName());
        assertEquals("EXT999", updated.getExternalId());
        assertEquals("Cantina XYZ", updated.getCafeteriaName());
        assertEquals("Rua Y, 456", updated.getLocation());
        assertEquals(120, updated.getStudentCount());
    }

    @Test
    void testUpdate_NotFound() {
        when(clientRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.update(1, client));
    }

    @Test
    void testDelete_Success() {
        when(clientRepository.existsById(1)).thenReturn(true);

        clientService.delete(1);

        verify(clientRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_NotFound() {
        when(clientRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> clientService.delete(1));
    }
}
