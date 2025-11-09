package com.vlupt.escola_api.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    public MetricsServiceImpl(ClientRepository clientRepository,
                              DataClientRepository dataClientRepository) {
        this.clientRepository = clientRepository;
        this.dataClientRepository = dataClientRepository;
    }

    @Override
    public List<MetricsDTO> calculateAllMetrics() {
        List<MetricsDTO> metricsList = new ArrayList<>();

        List<Client> clients = clientRepository.findAll();

        for (Client client : clients) {
            List<DataClient> dataList = dataClientRepository.findByClient_ClientId(client.getClientId());

            int totalStudentsSchool = client.getStudentCount();
            int totalStudentsRegistered = totalStudentsSchool;

            int totalOrdersMonth = dataList.stream()
                    .mapToInt(DataClient::getOrderCount)
                    .sum();

            BigDecimal totalRevenue = dataList.stream()
                    .map(DataClient::getRevenue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpenses = dataList.stream()
                    .map(DataClient::getExpenses)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal profit = totalRevenue.subtract(totalExpenses);

            BigDecimal profitPerStudent = totalStudentsRegistered > 0
                    ? profit.divide(BigDecimal.valueOf(totalStudentsRegistered), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal averageTicket = totalOrdersMonth > 0
                    ? totalRevenue.divide(BigDecimal.valueOf(totalOrdersMonth), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            double averageOrdersPerStudent = totalStudentsRegistered > 0
                    ? (double) totalOrdersMonth / totalStudentsRegistered
                    : 0.0;

            for (DataClient data : dataList) {
                metricsList.add(MetricsDTO.builder()
                        .schoolName(client.getSchoolName())
                        .totalStudentsSchool(totalStudentsSchool)
                        .totalStudentsRegistered(totalStudentsRegistered)
                        .profitPerStudent(profitPerStudent)
                        .averageTicket(averageTicket)
                        .averageOrdersPerStudent(averageOrdersPerStudent)
                        .totalOrdersMonth(data.getOrderCount())
                        .month(data.getMonthDate().getMonth())
                        .build());
            }
        }

        return metricsList;
    }
}
