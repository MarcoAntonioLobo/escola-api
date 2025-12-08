package com.vlupt.escola_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "client_data",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "month_date"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dataId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "month_date", nullable = false)
    private LocalDate monthDate;

    // Colunas da Cantina
    @Column(name = "cantina_percent", precision = 5, scale = 2)
    private BigDecimal cantinaPercent; // Vpt x Escola %

    @Column(name = "registered_students", nullable = false)
    private Integer registeredStudents; // L. Aluno Cad

    @Column(name = "average_cantina_per_student", precision = 10, scale = 2)
    private BigDecimal averageCantinaPerStudent; // T. Méd Cant.

    @Column(name = "average_pedagogical_per_student", precision = 10, scale = 2)
    private BigDecimal averagePedagogicalPerStudent; // Med. Ped. aluno

    @Column(name = "order_count")
    private Integer orderCount; // Qtde Pedido M

    @Column(name = "revenue", precision = 15, scale = 2)
    private BigDecimal revenue; // Faturamento

    // Colunas da Vlupt
    @Column(name = "profitability", precision = 10, scale = 2)
    private BigDecimal profitability; // Rentabilidade

    @Column(name = "revenue_loss", precision = 10, scale = 2)
    private BigDecimal revenueLoss; // Evasão de $$$

    @Column(name = "orders_outside_vpt")
    private Integer ordersOutsideVpt; // Ped. Fora Vpt

    @Column(name = "average_ticket_app", precision = 10, scale = 2)
    private BigDecimal averageTicketApp; // Ticket M. App.

    @Column(columnDefinition = "TEXT")
    private String notes;

    public Integer getYear() {
        return this.monthDate != null ? this.monthDate.getYear() : null;
    }
}
