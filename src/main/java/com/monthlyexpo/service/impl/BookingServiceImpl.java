package com.monthlyexpo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.monthlyexpo.dto.request.CustomerBulkPaymentRequest;
import com.monthlyexpo.dto.response.BookingResponse;
import com.monthlyexpo.dto.response.CustomerBookingSummaryResponse;
import com.monthlyexpo.entity.Customer;
import com.monthlyexpo.entity.TractorBooking;
import com.monthlyexpo.enums.DeliveryStatus;
import com.monthlyexpo.enums.PaymentStatus;
import com.monthlyexpo.repository.CustomerRepository;
import com.monthlyexpo.repository.TractorBookingRepository;
import com.monthlyexpo.service.BookingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final TractorBookingRepository bookingRepository;
    private final CustomerRepository customerRepository;

    @Override
    public TractorBooking completeDelivery(Long id) {
        TractorBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setDeliveryStatus(DeliveryStatus.COMPLETED);

        return bookingRepository.save(booking);
    }

    @Override
    public CustomerBookingSummaryResponse getCustomerBookings(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        List<TractorBooking> bookings =
                bookingRepository.findByCustomerId(customerId);

        BigDecimal totalAmount = bookings.stream()
                .map(TractorBooking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = bookings.stream()
                .map(TractorBooking::getAmountPaid)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalRemaining = totalAmount.subtract(totalPaid);

        String paymentStatus =
                totalRemaining.compareTo(BigDecimal.ZERO) == 0 ? "PAID"
                        : totalPaid.compareTo(BigDecimal.ZERO) == 0 ? "UNPAID"
                        : "PARTIAL";

        return CustomerBookingSummaryResponse.builder()
                .customerId(customer.getId())
                .customerName(customer.getName())
                .villageName(customer.getVillageName())
                .totalAmount(totalAmount)
                .totalPaid(totalPaid)
                .totalRemaining(totalRemaining)
                .paymentStatus(paymentStatus)
                .bookings(bookings.stream().map(this::toResponse).toList())
                .build();
    }

    @Override
    public CustomerBookingSummaryResponse payCustomerBookings(
            Long customerId,
            CustomerBulkPaymentRequest request) {

        BigDecimal remainingPayment = request.getAmount();

        List<TractorBooking> bookings =
                bookingRepository.findByCustomerId(customerId);

        for (TractorBooking booking : bookings) {

            if (remainingPayment.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            BigDecimal bookingRemaining =
                    booking.getTotalAmount().subtract(booking.getAmountPaid());

            if (bookingRemaining.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal payAmount =
                    remainingPayment.compareTo(bookingRemaining) >= 0
                            ? bookingRemaining
                            : remainingPayment;

            booking.setAmountPaid(booking.getAmountPaid().add(payAmount));
            remainingPayment = remainingPayment.subtract(payAmount);

            if (booking.getAmountPaid().compareTo(booking.getTotalAmount()) == 0) {
                booking.setPaymentStatus(PaymentStatus.PAID);
            } else if (booking.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
                booking.setPaymentStatus(PaymentStatus.PARTIAL);
            } else {
                booking.setPaymentStatus(PaymentStatus.UNPAID);
            }

            bookingRepository.save(booking);
        }

        return getCustomerBookings(customerId);
    }

    private BookingResponse toResponse(TractorBooking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .customer(b.getCustomer())
                .bookingDate(b.getBookingDate())
                .materialType(b.getMaterialType())
                .numberOfLoads(b.getNumberOfLoads())
                .pricePerLoad(b.getPricePerLoad())
                .totalAmount(b.getTotalAmount())
                .amountPaid(b.getAmountPaid())
                .remainingAmount(b.getRemainingAmount())
                .deliveryStatus(b.getDeliveryStatus())
                .paymentStatus(b.getPaymentStatus())
                .remarks(b.getRemarks())
                .build();
    }
}