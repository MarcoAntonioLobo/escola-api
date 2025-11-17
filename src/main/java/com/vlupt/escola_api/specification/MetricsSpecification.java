package com.vlupt.escola_api.specification;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vlupt.escola_api.model.DataClient;

import jakarta.persistence.criteria.Predicate;

public class MetricsSpecification {

    public static Specification<DataClient> filter(Long clientId, Month month, Integer year) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (clientId != null) {
                predicates.add(cb.equal(root.get("client").get("clientId"), clientId));
            }

            if (month != null) {
                predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("monthDate")), month.getValue()));
            }

            if (year != null) {
                predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("monthDate")), year));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
