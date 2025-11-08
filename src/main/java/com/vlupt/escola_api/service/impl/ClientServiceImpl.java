package com.vlupt.escola_api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Client> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Client save(Client client) {
        return repository.save(client);
    }

    @Override
    public Client update(Integer id, Client client) {
        Client existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));

        existing.setExternalId(client.getExternalId());
        existing.setSchoolName(client.getSchoolName());
        existing.setCafeteriaName(client.getCafeteriaName());
        existing.setLocation(client.getLocation());
        existing.setStudentCount(client.getStudentCount());

        return repository.save(existing);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente não encontrado com id: " + id);
        }
        repository.deleteById(id);
    }
}
