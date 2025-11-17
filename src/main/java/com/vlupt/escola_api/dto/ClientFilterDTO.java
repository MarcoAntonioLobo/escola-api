package com.vlupt.escola_api.dto;

import lombok.Data;

@Data
public class ClientFilterDTO {
    private Integer clientId;
    private String schoolName;
    private String externalId;
    private String location;
    private String cafeteriaName;
    private String sortBy;
    private String sortDirection;
}
