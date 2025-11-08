package com.vlupt.escola_api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vlupt.escola_api.exception.BadRequestException;
import com.vlupt.escola_api.exception.ConflictException;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.repository.DataClientRepository;
import com.vlupt.escola_api.service.DataClientService;

@Service
public class DataClientServiceImpl implements DataClientService {

    private final DataClientRepository repository;
    private final ClientRepository clientRepository;

    public DataClientServiceImpl(DataClientRepository repository,
                                 ClientRepository clientRepository) {
        this.repository = repository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<DataClient> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<DataClient> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public DataClient save(DataClient data) {
        validateClient(data.getClient());
        checkConflict(data);
        return repository.save(data);
    }

    @Override
    public DataClient update(Integer id, DataClient data) {
        DataClient existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Registro de dados não encontrado com id: " + id));

        validateClient(data.getClient());

        if (!existing.getMonthDate().equals(data.getMonthDate()) || 
            !existing.getClient().getClientId().equals(data.getClient().getClientId())) {
            checkConflict(data);
        }

        existing.setClient(data.getClient());
        existing.setMonthDate(data.getMonthDate());
        existing.setRevenue(data.getRevenue());
        existing.setExpenses(data.getExpenses());
        existing.setNotes(data.getNotes());

        return repository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Registro de dados não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public List<DataClient> findByClientId(Integer clientId) {
        return repository.findByClient_ClientId(clientId);
    }

    private void validateClient(Client client) {
        if (client == null || client.getClientId() == null) {
            throw new BadRequestException("Cliente não pode ser nulo");
        }

        if (!clientRepository.existsById(client.getClientId())) {
            throw new ResourceNotFoundException(
                    "Cliente não encontrado com id: " + client.getClientId());
        }
    }

    private void checkConflict(DataClient data) {
        boolean exists = repository.findByClient_ClientIdAndMonthDate(
                data.getClient().getClientId(), data.getMonthDate()).isPresent();
        if (exists) {
            throw new ConflictException(
                    "Já existe registro de dados para esse cliente neste mês: " + data.getMonthDate());
        }
    }
}
