package com.vlupt.escola_api.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vlupt.escola_api.dto.DataClientFilterDTO;
import com.vlupt.escola_api.model.DataClient;

import jakarta.persistence.criteria.Predicate;

public class DataClientSpecification {

    public static Specification<DataClient> filter(DataClientFilterDTO filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // FILTRO POR CLIENTE
            if (filter.getClientId() != null) {
                predicates.add(cb.equal(root.get("client").get("clientId"), filter.getClientId()));
            }

            // FILTRO POR DATA >=
            if (filter.getDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("monthDate"), filter.getDateStart()));
            }

            // FILTRO POR DATA <=
            if (filter.getDateEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("monthDate"), filter.getDateEnd()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
