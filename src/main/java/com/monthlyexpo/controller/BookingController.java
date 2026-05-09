package com.monthlyexpo.controller;

import com.monthlyexpo.dto.request.BookingRequest;
import com.monthlyexpo.dto.request.CustomerBulkPaymentRequest;
import com.monthlyexpo.dto.response.CustomerBookingSummaryResponse;
import com.monthlyexpo.dto.request.PaymentRequest;
import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.entity.Customer;
import com.monthlyexpo.entity.TractorBooking;
import com.monthlyexpo.entity.TractorPayment;
import com.monthlyexpo.enums.DeliveryStatus;
import com.monthlyexpo.repository.CustomerRepository;
import com.monthlyexpo.repository.TractorBookingRepository;
import com.monthlyexpo.repository.TractorPaymentRepository;
import com.monthlyexpo.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final TractorBookingRepository bookings;
    private final CustomerRepository customers;
    private final TractorPaymentRepository payments;
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BookingRequest request) {
        Customer customer = customers.findById(request.customerId()).orElseThrow();

        TractorBooking booking = TractorBooking.builder()
                .customer(customer)
                .bookingDate(request.bookingDate())
                .materialType(request.materialType())
                .numberOfLoads(request.numberOfLoads())
                .pricePerLoad(request.pricePerLoad())
                .amountPaid(request.amountPaid())
                .remarks(request.remarks())
                .deliveryStatus(DeliveryStatus.PENDING)
                .build();

        booking.recalculate();

        return ResponseEntity.ok(
                ApiResponse.ok("Booking created", bookings.save(booking))
        );
    }

    @GetMapping
    public ResponseEntity<?> all() {
        return ResponseEntity.ok(
                ApiResponse.ok("Bookings", bookings.findAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Booking", bookings.findById(id).orElseThrow())
        );
    }

    @PatchMapping("/{id}/delivered")
    public ResponseEntity<?> delivered(@PathVariable Long id) {
        TractorBooking booking = bookings.findById(id).orElseThrow();

        booking.setDeliveryStatus(DeliveryStatus.COMPLETED);
        booking.recalculate();

        TractorBooking saved = bookings.save(booking);

        return ResponseEntity.ok(
                ApiResponse.ok("Marked delivered", saved)
        );
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> pay(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {

        TractorBooking booking = bookings.findById(id).orElseThrow();

        TractorPayment payment = payments.save(
                TractorPayment.builder()
                        .booking(booking)
                        .paymentDate(request.paymentDate())
                        .amount(request.amount())
                        .paymentMode(request.paymentMode())
                        .notes(request.notes())
                        .build()
        );

        booking.setAmountPaid(booking.getAmountPaid().add(request.amount()));
        booking.recalculate();
        bookings.save(booking);

        return ResponseEntity.ok(
                ApiResponse.ok("Payment added", payment)
        );
    }

    @GetMapping("/{id}/payments")
    public ResponseEntity<?> paymentHistory(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok("Payments", payments.findByBookingId(id))
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(required = false) String village,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {

        return ResponseEntity.ok(
                ApiResponse.ok("Bookings", bookings.filter(village, from, to))
        );
    }
    
    @GetMapping("/customer/{customerId}")
    public ApiResponse<CustomerBookingSummaryResponse> getCustomerBookings(
            @PathVariable Long customerId) {

        return ApiResponse.ok(
                "Customer bookings fetched",
                bookingService.getCustomerBookings(customerId)
        );
    }
    @PostMapping("/customer/{customerId}/payments")
    public ApiResponse<CustomerBookingSummaryResponse> payCustomerBookings(
            @PathVariable Long customerId,
            @RequestBody CustomerBulkPaymentRequest request) {

        return ApiResponse.ok(
                "Customer payment added",
                bookingService.payCustomerBookings(customerId, request)
        );
    }
}