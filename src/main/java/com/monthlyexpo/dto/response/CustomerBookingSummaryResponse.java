package com.monthlyexpo.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CustomerBookingSummaryResponse {
    private Long customerId;
    private String customerName;
    private String villageName;
    private BigDecimal totalAmount;
    private BigDecimal totalPaid;
    private BigDecimal totalRemaining;
    private String paymentStatus;
    private List<BookingResponse> bookings;
}