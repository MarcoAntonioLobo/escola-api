package com.vlupt.escola_api.service;

import java.util.List;
import java.util.Optional;

import com.vlupt.escola_api.dto.DataClientFilterDTO;
import com.vlupt.escola_api.model.DataClient;

public interface DataClientService {
    
    List<DataClient> findAll();

    Optional<DataClient> findById(Integer id);

    DataClient save(DataClient data);

    DataClient update(Integer id, DataClient data);

    void delete(Integer id);

    // FILTRO COM SPECIFICATION
    List<DataClient> filter(DataClientFilterDTO filter);

    // LISTAR DADOS POR CLIENTE
    List<DataClient> findByClientId(Integer clientId);
}
