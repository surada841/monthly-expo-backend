package com.monthlyexpo.controller;

import com.monthlyexpo.dto.request.HomeExpenseRequest;
import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.entity.HomeExpense;
import com.monthlyexpo.repository.HomeExpenseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/home-expenses")
@RequiredArgsConstructor
public class HomeExpenseController {

    private final HomeExpenseRepository repo;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody HomeExpenseRequest request) {
        log.info("Creating home expense category={} amount={} date={}", request.category(), request.amount(), request.expenseDate());
        HomeExpense saved = repo.save(HomeExpense.builder()
                .expenseDate(request.expenseDate())
                .category(request.category())
                .amount(request.amount())
                .description(request.description())
                .build());
        log.info("Home expense created id={} amount={}", saved.getId(), saved.getAmount());
        return ResponseEntity.ok(ApiResponse.ok("Home expense added", saved));
    }

    @GetMapping
    public ResponseEntity<?> all() {
        log.debug("Fetching all home expenses");
        return ResponseEntity.ok(ApiResponse.ok("Home expenses", repo.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.warn("Deleting home expense id={}", id);
        repo.deleteById(id);
        log.info("Home expense deleted id={}", id);
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}
