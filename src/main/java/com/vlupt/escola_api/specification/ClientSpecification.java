package com.vlupt.escola_api.specification;

import org.springframework.data.jpa.domain.Specification;
import com.vlupt.escola_api.dto.ClientFilterDTO;
import com.vlupt.escola_api.model.Client;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ClientSpecification {

    public static Specification<Client> filter(ClientFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId"), filter.getClientId()));
            }

            if (filter.getSchoolName() != null && !filter.getSchoolName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("schoolName")), "%" + filter.getSchoolName().toLowerCase() + "%"));
            }

            if (filter.getExternalId() != null && !filter.getExternalId().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("externalId")), "%" + filter.getExternalId().toLowerCase() + "%"));
            }

            if (filter.getCafeteriaName() != null && !filter.getCafeteriaName().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("cafeteriaName")), "%" + filter.getCafeteriaName().toLowerCase() + "%"));
            }

            if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("location")), "%" + filter.getLocation().toLowerCase() + "%"));
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
