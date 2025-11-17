package com.vlupt.escola_api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.vlupt.escola_api.dto.ClientFilterDTO;
import com.vlupt.escola_api.exception.ResourceNotFoundException;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.service.ClientService;
import com.vlupt.escola_api.specification.ClientSpecification;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Client> filter(ClientFilterDTO filter) {
        // Ajusta sort padrão
        Sort sort = Sort.unsorted();
        if (filter.getSortBy() != null && !filter.getSortBy().isBlank()) {
            // Verifica se o campo existe na entidade Client antes de aplicar o sort
            switch (filter.getSortBy()) {
                case "clientId":
                case "schoolName":
                case "externalId":
                case "cafeteriaName":
                case "location":
                case "studentCount":
                    sort = "desc".equalsIgnoreCase(filter.getSortDirection())
                            ? Sort.by(filter.getSortBy()).descending()
                            : Sort.by(filter.getSortBy()).ascending();
                    break;
                default:
                    // Campo inválido: não aplica sort
                    sort = Sort.unsorted();
                    break;
            }
        }

        // Se clientId for null ou 0, ignora filtro
        if (filter.getClientId() != null && filter.getClientId() == 0) {
            filter.setClientId(null);
        }

        return repository.findAll(ClientSpecification.filter(filter), sort);
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
