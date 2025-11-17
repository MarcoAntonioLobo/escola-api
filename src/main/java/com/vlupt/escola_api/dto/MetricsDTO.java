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

	private String schoolName;
	
	private int totalStudentsSchool;
	
	private int totalStudentsRegistered;
	
	private BigDecimal studentsRegisteredVsTotal;

	private BigDecimal profitPerStudent;

	private BigDecimal averageTicket;

	private double averageOrdersPerStudent;

	private int totalOrdersMonth;

	private Month month;
}