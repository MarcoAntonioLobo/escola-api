package com.vlupt.escola_api.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.vlupt.escola_api.dto.MetricsFilterDTO;
import com.vlupt.escola_api.model.DataClient;

import jakarta.persistence.criteria.Predicate;

public class MetricsSpecification {

    public static Specification<DataClient> filter(MetricsFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getSchoolName() != null && !filter.getSchoolName().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("client").get("schoolName")),
                                       "%" + filter.getSchoolName().toLowerCase() + "%"));
            }

            if (filter.getMonth() != null) {
                predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("monthDate")),
                                       filter.getMonth().getValue()));
            }

            if (filter.getYear() != null) {
                predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("monthDate")),
                                       filter.getYear()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
