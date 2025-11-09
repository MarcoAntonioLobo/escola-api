package com.vlupt.escola_api.dto;

import java.math.BigDecimal;
import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricsDTO {

    // 1 e 6: Alunos
    private String schoolName;
    private int totalStudentsSchool;        // Total de alunos na escola
    private int totalStudentsRegistered;    // Alunos cadastrados na Vlupt

    // 2: Lucro por aluno cadastrado
    private BigDecimal profitPerStudent;

    // 3 e 7: Ticket médio cantina
    private BigDecimal averageTicket;

    // 4 e 8: Média de pedidos por aluno cadastrado
    private double averageOrdersPerStudent;

    // 5 e 9: Quantidade de pedidos por mês
    private int totalOrdersMonth;

    // Mês referente à métrica
    private Month month;
}
