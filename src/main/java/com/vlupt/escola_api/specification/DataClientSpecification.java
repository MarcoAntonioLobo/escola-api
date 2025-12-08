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

            if (filter.getClientId() != null) {
                predicates.add(cb.equal(root.get("client").get("clientId"), filter.getClientId()));
            }

            if (filter.getDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("monthDate"), filter.getDateStart()));
            }

            if (filter.getDateEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("monthDate"), filter.getDateEnd()));
            }

            if (filter.getLocation() != null && !filter.getLocation().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("client").get("location")),
                        "%" + filter.getLocation().toLowerCase() + "%"));
            }

            if (filter.getSchool() != null && !filter.getSchool().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("client").get("schoolName")),
                        "%" + filter.getSchool().toLowerCase() + "%"));
            }

            if (filter.getCafeteria() != null && !filter.getCafeteria().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("client").get("cafeteriaName")),
                        "%" + filter.getCafeteria().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
