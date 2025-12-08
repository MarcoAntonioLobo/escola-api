package com.vlupt.escola_api.dto;

import java.time.LocalDate;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataClientFilterDTO {

    private Integer clientId;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String sortBy;
    private String direction;
    private String location;
    private String school;
    private String cafeteria;
}
