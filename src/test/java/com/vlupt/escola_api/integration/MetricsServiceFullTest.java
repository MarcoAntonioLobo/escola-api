package com.vlupt.escola_api.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vlupt.escola_api.dto.MetricsDTO;
import com.vlupt.escola_api.model.Client;
import com.vlupt.escola_api.model.DataClient;
import com.vlupt.escola_api.repository.ClientRepository;
import com.vlupt.escola_api.repository.DataClientRepository;
import com.vlupt.escola_api.service.impl.MetricsServiceImpl;

class MetricsServiceFullTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private DataClientRepository dataClientRepository;

    @InjectMocks
    private MetricsServiceImpl metricsService;

    private List<Client> clients;
    private List<DataClient> allDataClients;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        clients = new ArrayList<>();
        allDataClients = new ArrayList<>();

        // ======= CLIENTES =======
        clients.add(Client.builder().clientId(1).schoolName("Escola A").studentCount(1000).build());
        clients.add(Client.builder().clientId(2).schoolName("Escola B").studentCount(500).build());

        when(clientRepository.findAll()).thenReturn(clients);

        // ======= DADOS =======
        for (Client client : clients) {
            List<DataClient> clientData = new ArrayList<>();

            if (client.getClientId() == 1) {
                clientData.add(createDataClient(client, 1, LocalDate.of(2025, 9, 1), 10000, 2000, 100, 900));
                clientData.add(createDataClient(client, 2, LocalDate.of(2025, 10, 1), 15000, 5000, 150, 950));
            } else if (client.getClientId() == 2) {
                clientData.add(createDataClient(client, 3, LocalDate.of(2025, 9, 1), 5000, 1000, 50, 450));
            }

            allDataClients.addAll(clientData);
            when(dataClientRepository.findByClient_ClientId(client.getClientId())).thenReturn(clientData);
        }
    }

    private DataClient createDataClient(Client client, int dataId, LocalDate month,
                                        double revenue, double expenses, int orderCount, int registeredStudents) {
        return DataClient.builder()
                .dataId(dataId)
                .client(client)
                .monthDate(month)
                .revenue(BigDecimal.valueOf(revenue))
                .expenses(BigDecimal.valueOf(expenses))
                .orderCount(orderCount)
                .registeredStudents(registeredStudents)
                .build();
    }

    @Test
    void testCalculateAllMetrics() {
        List<MetricsDTO> metrics = metricsService.calculateAllMetrics();

        for (MetricsDTO metric : metrics) {

            DataClient data = allDataClients.stream()
                    .filter(d -> d.getClient().getSchoolName().equals(metric.getSchoolName()))
                    .filter(d -> d.getMonthDate().getMonth().equals(metric.getMonth()))
                    .findFirst()
                    .orElseThrow();

            BigDecimal profitPerStudent = data.getRegisteredStudents() > 0
                    ? data.getRevenue().subtract(data.getExpenses())
                          .divide(BigDecimal.valueOf(data.getRegisteredStudents()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal averageTicket = data.getOrderCount() > 0
                    ? data.getRevenue().divide(BigDecimal.valueOf(data.getOrderCount()), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            double avgOrdersPerStudent = data.getRegisteredStudents() > 0
                    ? (double) data.getOrderCount() / data.getRegisteredStudents()
                    : 0.0;

            assertEquals(profitPerStudent, metric.getProfitPerStudent(),
                    "Profit per student mismatch for " + metric.getSchoolName());
            assertEquals(averageTicket, metric.getAverageTicket(),
                    "Average ticket mismatch for " + metric.getSchoolName());
            assertEquals(avgOrdersPerStudent, metric.getAverageOrdersPerStudent(), 0.01,
                    "Average orders per student mismatch for " + metric.getSchoolName());
        }
    }

}
