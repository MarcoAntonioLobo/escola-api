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
        clients.add(Client.builder().clientId(8).schoolName("Gluck Haus Cafe Marista Paranaense").studentCount(2609).build());
        clients.add(Client.builder().clientId(9).schoolName("Cantina Santa Maria Marista Santa Maria").studentCount(2428).build());
        clients.add(Client.builder().clientId(22).schoolName("Pôla Lanches Positivo Júnior").studentCount(1557).build());
        clients.add(Client.builder().clientId(23).schoolName("Pôla Lanches Positivo Ângelo Sampaio").studentCount(908).build());
        clients.add(Client.builder().clientId(24).schoolName("Gluck Haus Colégio Bom Jesus Centro").studentCount(1387).build());
        clients.add(Client.builder().clientId(30).schoolName("Cantina Bon Bini Colégio Marista Anjo da Guarda").studentCount(1123).build());
        clients.add(Client.builder().clientId(36).schoolName("Cardapium Bom Jesus Divina Providência").studentCount(812).build());
        clients.add(Client.builder().clientId(38).schoolName("Cantina Arte & Sabor Colégio Adventista Alto Boqueirão").studentCount(1250).build());
        clients.add(Client.builder().clientId(53).schoolName("Cantina da Nona Escola Bambinata").studentCount(600).build());
        clients.add(Client.builder().clientId(59).schoolName("Graces Coffee Escola Grace").studentCount(620).build());
        clients.add(Client.builder().clientId(60).schoolName("Cantinho do Sabor Curso e Colégio Acesso").studentCount(545).build());
        clients.add(Client.builder().clientId(67).schoolName("Eskillus Escola Interativa - Bacacheri").studentCount(354).build());
        clients.add(Client.builder().clientId(100).schoolName("Red House Red House - Campus I").studentCount(200).build());
        clients.add(Client.builder().clientId(101).schoolName("Cantina Solos Escola Solos").studentCount(330).build());
        clients.add(Client.builder().clientId(110).schoolName("Coração da Nonna Santa Teresinha Escola Santa Teresinha do Menino Jesus").studentCount(130).build());
        clients.add(Client.builder().clientId(213).schoolName("Cantina Escolar Cavalcanti Colégio Amplação").studentCount(926).build());
        clients.add(Client.builder().clientId(245).schoolName("Maaloe Red House - Campus 2").studentCount(200).build());
        clients.add(Client.builder().clientId(251).schoolName("La Merienda Gastronomia Colegio Assunção").studentCount(1000).build());
        clients.add(Client.builder().clientId(352).schoolName("Cantina Lumen Escola Lumen Ltda").studentCount(241).build());
        clients.add(Client.builder().clientId(381).schoolName("Cantina Cosmos Escola Cosmos").studentCount(158).build());
        clients.add(Client.builder().clientId(388).schoolName("Cantina Escola Sao Carlos Borromeo").studentCount(450).build());

        // ======= MOCK CLIENT REPOSITORY =======
        when(clientRepository.findAll()).thenReturn(clients);

        // ======= MOCK DATA CLIENT =======
        for (Client client : clients) {
            List<DataClient> clientData = new ArrayList<>();
            switch (client.getClientId()) {
                case 8:
                    clientData.add(createDataClient(client, 1, LocalDate.of(2025, 9, 1), 159611.53, 7851.70, 14333));
                    clientData.add(createDataClient(client, 2, LocalDate.of(2025, 10, 1), 160000, 8000, 14500));
                    break;
                case 9:
                    clientData.add(createDataClient(client, 3, LocalDate.of(2025, 9, 1), 65569.50, 2668.14, 7944));
                    clientData.add(createDataClient(client, 4, LocalDate.of(2025, 10, 1), 66000, 2700, 8000));
                    break;
                case 22:
                    clientData.add(createDataClient(client, 5, LocalDate.of(2025, 9, 1), 125315.40, 4637.75, 13478));
                    break;
                case 23:
                    clientData.add(createDataClient(client, 6, LocalDate.of(2025, 9, 1), 6975.20, 229.54, 651));
                    break;
                case 24:
                    clientData.add(createDataClient(client, 7, LocalDate.of(2025, 9, 1), 4596, 521.85, 447));
                    break;
                case 30:
                    clientData.add(createDataClient(client, 8, LocalDate.of(2025, 9, 1), 151551, 974.38, 14497));
                    break;
                case 36:
                    clientData.add(createDataClient(client, 9, LocalDate.of(2025, 9, 1), 49408.50, 2620.11, 5135));
                    break;
                case 38:
                    clientData.add(createDataClient(client, 10, LocalDate.of(2025, 9, 1), 21286.29, 1403.98, 4501));
                    break;
                case 53:
                    clientData.add(createDataClient(client, 11, LocalDate.of(2025, 9, 1), 26702, 801.14, 3161));
                    break;
                case 59:
                    clientData.add(createDataClient(client, 12, LocalDate.of(2025, 9, 1), 22596.85, 713.36, 2822));
                    break;
                case 60:
                    clientData.add(createDataClient(client, 13, LocalDate.of(2025, 9, 1), 13739.50, 180.64, 1258));
                    break;
                case 67:
                    clientData.add(createDataClient(client, 14, LocalDate.of(2025, 9, 1), 5332.70, 159.57, 733));
                    break;
                case 100:
                    clientData.add(createDataClient(client, 15, LocalDate.of(2025, 9, 1), 9393.50, 349.38, 695));
                    break;
                case 101:
                    clientData.add(createDataClient(client, 16, LocalDate.of(2025, 9, 1), 69592.36, 1026.57, 4659));
                    break;
                case 110:
                    clientData.add(createDataClient(client, 17, LocalDate.of(2025, 9, 1), 4833.80, 522.38, 590));
                    break;
                case 213:
                    clientData.add(createDataClient(client, 18, LocalDate.of(2025, 9, 1), 21811, 1323.34, 2715));
                    break;
                case 245:
                    clientData.add(createDataClient(client, 19, LocalDate.of(2025, 9, 1), 16040.50, 855.11, 1386));
                    break;
                case 251:
                    clientData.add(createDataClient(client, 20, LocalDate.of(2025, 9, 1), 17015.50, 1172.66, 2516));
                    break;
                case 352:
                    clientData.add(createDataClient(client, 21, LocalDate.of(2025, 9, 1), 15256.40, 445.78, 1592));
                    break;
            }
            allDataClients.addAll(clientData);
            when(dataClientRepository.findByClient_ClientId(client.getClientId())).thenReturn(clientData);
        }
    }

    private DataClient createDataClient(Client client, int dataId, LocalDate month, double revenue, double expenses, int orderCount) {
        return DataClient.builder()
                .dataId(dataId)
                .client(client)
                .monthDate(month)
                .revenue(BigDecimal.valueOf(revenue))
                .expenses(BigDecimal.valueOf(expenses))
                .orderCount(orderCount)
                .build();
    }

    @Test
    void testAllMetrics() {
        List<MetricsDTO> metrics = metricsService.calculateAllMetrics();

        for (MetricsDTO metric : metrics) {
            Client client = clients.stream()
                    .filter(c -> c.getSchoolName().equals(metric.getSchoolName()))
                    .findFirst().orElseThrow();

            // Calcula métricas esperadas
            List<DataClient> clientData = allDataClients.stream()
                    .filter(d -> d.getClient().getClientId().equals(client.getClientId()))
                    .toList();

            int totalStudents = client.getStudentCount();
            int totalOrders = clientData.stream().mapToInt(DataClient::getOrderCount).sum();
            BigDecimal totalRevenue = clientData.stream().map(DataClient::getRevenue).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalExpenses = clientData.stream().map(DataClient::getExpenses).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal profitPerStudent = totalStudents > 0
                    ? totalRevenue.subtract(totalExpenses).divide(BigDecimal.valueOf(totalStudents), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            BigDecimal averageTicket = totalOrders > 0
                    ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            double avgOrdersPerStudent = totalStudents > 0 ? (double) totalOrders / totalStudents : 0.0;

            assertEquals(profitPerStudent, metric.getProfitPerStudent(), "Profit per student mismatch for " + metric.getSchoolName());
            assertEquals(averageTicket, metric.getAverageTicket(), "Average ticket mismatch for " + metric.getSchoolName());
            assertEquals(avgOrdersPerStudent, metric.getAverageOrdersPerStudent(), 0.01, "Average orders per student mismatch for " + metric.getSchoolName());
        }
    }
}
