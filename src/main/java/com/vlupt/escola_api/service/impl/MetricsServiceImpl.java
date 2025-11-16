package com.vlupt.escola_api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vlupt.escola_api.dto.MetricsDTO;
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

    // ============================================
    // MÉTODOS DE FILTRO
    // ============================================
    @Override
    public List<MetricsDTO> getMetricsFiltered(Long clientId, Month month, Integer year) {
        List<DataClient> data = dataClientRepository.findAll();
        data = applyFilters(data, clientId, month, year);
        return convertToMetricsDTO(data);
    }

    @Override
    public Page<MetricsDTO> getMetricsFilteredPaged(Long clientId, Month month, Integer year, Pageable pageable) {
        List<DataClient> data = dataClientRepository.findAll();
        data = applyFilters(data, clientId, month, year);

        int start = Math.min((int) pageable.getOffset(), data.size());
        int end = Math.min(start + pageable.getPageSize(), data.size());
        List<MetricsDTO> pagedList = convertToMetricsDTO(data.subList(start, end));

        return new PageImpl<>(pagedList, pageable, data.size());
    }

    private List<DataClient> applyFilters(List<DataClient> data, Long clientId, Month month, Integer year) {
        if (clientId != null) {
            data = data.stream()
                    .filter(dc -> dc.getClient().getClientId().longValue() == clientId)
                    .collect(Collectors.toList());
        }
        if (month != null) {
            data = data.stream()
                    .filter(dc -> dc.getMonthDate().getMonth().equals(month))
                    .collect(Collectors.toList());
        }
        if (year != null) {
            data = data.stream()
                    .filter(dc -> dc.getMonthDate().getYear() == year)
                    .collect(Collectors.toList());
        }
        return data;
    }

    // ============================================
    // CONVERSÃO PARA DTO
    // ============================================
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

    // ============================================
    // TODAS AS MÉTRICAS (SEM FILTRO)
    // ============================================
    @Override
    public List<MetricsDTO> calculateAllMetrics() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .flatMap(client -> dataClientRepository.findByClient_ClientId(client.getClientId()).stream())
                .map(d -> convertToMetricsDTO(List.of(d)).get(0))
                .collect(Collectors.toList());
    }
}