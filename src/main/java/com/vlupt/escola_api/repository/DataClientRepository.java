package com.vlupt.escola_api.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vlupt.escola_api.model.DataClient;

@Repository
public interface DataClientRepository
        extends JpaRepository<DataClient, Integer>, JpaSpecificationExecutor<DataClient> {

    List<DataClient> findByClient_ClientId(Integer clientId);

    Optional<DataClient> findByClient_ClientIdAndMonthDate(Integer clientId, LocalDate date);
}
