package com.monthlyexpo.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.monthlyexpo.entity.Customer;
import com.monthlyexpo.enums.DeliveryStatus;
import com.monthlyexpo.enums.MaterialType;
import com.monthlyexpo.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {

    private Long id;

    private Customer customer;

    private LocalDate bookingDate;

    private MaterialType materialType;

    private Integer numberOfLoads;

    private BigDecimal pricePerLoad;

    private BigDecimal totalAmount;

    private BigDecimal amountPaid;

    private BigDecimal remainingAmount;

    private DeliveryStatus deliveryStatus;

    private PaymentStatus paymentStatus;

    private String remarks;
}