package com.vlupt.escola_api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.dto.MetricsFilterDTO;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.repository.DataClientRepository;
import com.vlupt.escola_api.service.MetricsService;

@Service
public class MetricsServiceImpl implements MetricsService {

    private final ClientRepository clientRepository;
    private final DataClientRepository dataClientRepository;

    public MetricsServiceImpl(ClientRepository clientRepository, DataClientRepository dataClientRepository) {
        this.clientRepository = clientRepository;
        this.dataClientRepository = dataClientRepository;
    }

    @Override
    public List<MetricsDTO> getMetricsFiltered(MetricsFilterDTO filter) {
        List<DataClient> data = dataClientRepository.findAll();
        data = applyFilters(data, filter);
        data = applySorting(data, filter);
        return convertToMetricsDTO(data);
    }

    @Override
    public Page<MetricsDTO> getMetricsFilteredPaged(MetricsFilterDTO filter, Pageable pageable) {
        List<DataClient> data = dataClientRepository.findAll();
        data = applyFilters(data, filter);
        data = applySorting(data, filter);

        int start = Math.min((int) pageable.getOffset(), data.size());
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<MetricsDTO> pagedList = convertToMetricsDTO(data.subList(start, end));

        return new PageImpl<>(pagedList, pageable, data.size());
    }

    private List<DataClient> applyFilters(List<DataClient> data, MetricsFilterDTO filter) {
        if (filter.getSchoolName() != null && !filter.getSchoolName().isEmpty()) {
            data = data.stream()
                    .filter(dc -> dc.getClient().getSchoolName().toLowerCase()
                                   .contains(filter.getSchoolName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (filter.getMonth() != null) {
            data = data.stream()
                    .filter(dc -> dc.getMonthDate().getMonth().equals(filter.getMonth()))
                    .collect(Collectors.toList());
        }
        if (filter.getYear() != null) {
            data = data.stream()
                    .filter(dc -> dc.getMonthDate().getYear() == filter.getYear())
                    .collect(Collectors.toList());
        }
        return data;
    }

    private List<DataClient> applySorting(List<DataClient> data, MetricsFilterDTO filter) {
        if (filter.getSortBy() != null && !filter.getSortBy().isEmpty()) {
            Comparator<DataClient> comparator;

            switch (filter.getSortBy()) {
                case "schoolName":
                    comparator = Comparator.comparing(dc -> dc.getClient().getSchoolName(), String.CASE_INSENSITIVE_ORDER);
                    break;
                case "totalStudentsRegistered":
                    comparator = Comparator.comparing(DataClient::getRegisteredStudents);
                    break;
                case "totalOrdersMonth":
                    comparator = Comparator.comparing(DataClient::getOrderCount);
                    break;
                case "profitPerStudent":
                    comparator = Comparator.comparing(dc -> dc.getRevenue().subtract(dc.getExpenses())
                            .divide(BigDecimal.valueOf(dc.getRegisteredStudents() > 0 ? dc.getRegisteredStudents() : 1), 2, RoundingMode.HALF_UP));
                    break;
                default:
                    comparator = Comparator.comparing(dc -> dc.getMonthDate().getMonthValue());
            }

            if ("desc".equalsIgnoreCase(filter.getSortDirection())) {
                comparator = comparator.reversed();
            }

            data = data.stream().sorted(comparator).collect(Collectors.toList());
        }
        return data;
    }

    private List<MetricsDTO> convertToMetricsDTO(List<DataClient> data) {
        return data.stream().map(d -> {
            Client client = d.getClient();
            int totalStudentsSchool = client.getStudentCount();
            int totalStudentsRegistered = d.getRegisteredStudents();

            BigDecimal studentsRegisteredVsTotal = totalStudentsSchool > 0
                    ? BigDecimal.valueOf(totalStudentsRegistered)
                               .multiply(BigDecimal.valueOf(100))
                               .divide(BigDecimal.valueOf(totalStudentsSchool), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal profit = d.getRevenue().subtract(d.getExpenses());
            BigDecimal profitPerStudent = totalStudentsRegistered > 0
                    ? profit.divide(BigDecimal.valueOf(totalStudentsRegistered), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal averageTicket = d.getOrderCount() > 0
                    ? d.getRevenue().divide(BigDecimal.valueOf(d.getOrderCount()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            double averageOrdersPerStudent = totalStudentsRegistered > 0
                    ? (double) d.getOrderCount() / totalStudentsRegistered
                    : 0.0;

            return MetricsDTO.builder()
                    .schoolName(client.getSchoolName())
                    .totalStudentsSchool(totalStudentsSchool)
                    .totalStudentsRegistered(totalStudentsRegistered)
                    .studentsRegisteredVsTotal(studentsRegisteredVsTotal)
                    .profitPerStudent(profitPerStudent)
                    .averageTicket(averageTicket)
                    .averageOrdersPerStudent(averageOrdersPerStudent)
                    .totalOrdersMonth(d.getOrderCount())
                    .month(d.getMonthDate().getMonth())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<MetricsDTO> calculateAllMetrics() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .flatMap(client -> dataClientRepository.findByClient_ClientId(client.getClientId()).stream())
                .map(d -> convertToMetricsDTO(List.of(d)).get(0))
                .collect(Collectors.toList());
    }
}
