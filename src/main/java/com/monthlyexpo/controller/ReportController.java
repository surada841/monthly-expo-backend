package com.monthlyexpo.controller;

import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.dto.response.DashboardResponse;
import com.monthlyexpo.repository.HomeExpenseRepository;
import com.monthlyexpo.repository.TractorBookingRepository;
import com.monthlyexpo.repository.TractorExpenseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final TractorBookingRepository bookings;
    private final TractorExpenseRepository tractorExpenses;
    private final HomeExpenseRepository homeExpenses;

    @GetMapping("/monthly")
    public ResponseEntity<?> monthly(@RequestParam int month, @RequestParam int year) {
        log.info("Generating monthly report for month={} year={}", month, year);
        LocalDate from = LocalDate.of(year, month, 1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
        BigDecimal income = bookings.paidIncomeBetween(from, to);
        BigDecimal tractorExpense = tractorExpenses.totalBetween(from, to);
        BigDecimal homeExpense = homeExpenses.totalBetween(from, to);
        DashboardResponse response = new DashboardResponse(income, tractorExpense, income.subtract(tractorExpense), homeExpense);
        log.info("Monthly report generated month={} year={} income={} tractorExpense={} profit={} homeExpense={}",
                month, year, income, tractorExpense, response.tractorProfit(), homeExpense);
        return ResponseEntity.ok(ApiResponse.ok("Monthly report", response));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        LocalDate from = LocalDate.now().withDayOfMonth(1);
        LocalDate to = from.withDayOfMonth(from.lengthOfMonth());
        log.debug("Generating dashboard report from={} to={}", from, to);
        BigDecimal income = bookings.paidIncomeBetween(from, to);
        BigDecimal tractorExpense = tractorExpenses.totalBetween(from, to);
        BigDecimal homeExpense = homeExpenses.totalBetween(from, to);
        DashboardResponse response = new DashboardResponse(income, tractorExpense, income.subtract(tractorExpense), homeExpense);
        log.info("Dashboard report generated income={} tractorExpense={} profit={} homeExpense={}",
                income, tractorExpense, response.tractorProfit(), homeExpense);
        return ResponseEntity.ok(ApiResponse.ok("Dashboard", response));
    }
}
