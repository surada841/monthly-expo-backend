package com.monthlyexpo.dto.request;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerBulkPaymentRequest {
    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentMode;
    private String notes;
}