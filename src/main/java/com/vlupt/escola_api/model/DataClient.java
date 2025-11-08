package com.vlupt.escola_api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "month_date", nullable = false)
    private LocalDate monthDate;

    @Builder.Default
    private BigDecimal revenue = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal expenses = BigDecimal.ZERO;

    @Column(columnDefinition = "TEXT")
    private String notes;
}
