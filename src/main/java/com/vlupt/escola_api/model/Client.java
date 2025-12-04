package com.vlupt.escola_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clientId;

    @Column(name = "external_id", length = 255)
    private String externalId;

    @Column(name = "school_name", nullable = false, length = 255)
    private String schoolName;

    @Column(name = "cafeteria_name", length = 255)
    private String cafeteriaName;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "student_count")
    private Integer studentCount;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DataClient> dataClients;
}
