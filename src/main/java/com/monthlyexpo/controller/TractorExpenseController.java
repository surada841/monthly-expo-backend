package com.monthlyexpo.controller;

import com.monthlyexpo.dto.request.TractorExpenseRequest;
import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.entity.TractorExpense;
import com.monthlyexpo.repository.TractorExpenseRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/tractor-expenses")
@RequiredArgsConstructor
public class TractorExpenseController {

    private final TractorExpenseRepository repo;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TractorExpenseRequest request) {
        log.info("Creating tractor expense category={} amount={} date={}", request.category(), request.amount(), request.expenseDate());
        TractorExpense saved = repo.save(TractorExpense.builder()
                .expenseDate(request.expenseDate())
                .category(request.category())
                .amount(request.amount())
                .description(request.description())
                .build());
        log.info("Tractor expense created id={} category={} amount={}", saved.getId(), saved.getCategory(), saved.getAmount());
        return ResponseEntity.ok(ApiResponse.ok("Expense added", saved));
    }

    @GetMapping
    public ResponseEntity<?> all() {
        log.debug("Fetching all tractor expenses");
        return ResponseEntity.ok(ApiResponse.ok("Expenses", repo.findAll()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.warn("Deleting tractor expense id={}", id);
        repo.deleteById(id);
        log.info("Tractor expense deleted id={}", id);
        return ResponseEntity.ok(ApiResponse.ok("Deleted", null));
    }
}
