package com.vlupt.escola_api.service;

import java.util.List;
import java.util.Optional;

import com.vlupt.escola_api.dto.ClientFilterDTO;
import com.vlupt.escola_api.model.Client;

public interface ClientService {

    List<Client> findAll();

    Optional<Client> findById(Integer id);

    Client save(Client client);

    Client update(Integer id, Client client);

    void delete(Integer id);
    
    List<Client> filter(ClientFilterDTO filter);
}
