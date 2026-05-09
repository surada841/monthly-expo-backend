package com.monthlyexpo.controller;

import com.monthlyexpo.dto.request.CustomerRequest;
import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.entity.Customer;
import com.monthlyexpo.repository.CustomerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository repo;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerRequest request) {
        log.info("Creating customer name={} village={}", request.name(), request.villageName());
        Customer saved = repo.save(Customer.builder()
                .name(request.name())
                .mobileNumber(request.mobileNumber())
                .villageName(request.villageName())
                .build());
        log.info("Customer created with id={} village={}", saved.getId(), saved.getVillageName());
        return ResponseEntity.ok(ApiResponse.ok("Customer added", saved));
    }

    @GetMapping
    public ResponseEntity<?> all() {
        log.debug("Fetching all customers");
        return ResponseEntity.ok(ApiResponse.ok("Customers", repo.findAll()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String name) {
        log.debug("Searching customers by name={}", name);
        return ResponseEntity.ok(ApiResponse.ok("Customers", repo.findByNameContainingIgnoreCase(name)));
    }

    @GetMapping("/village")
    public ResponseEntity<?> village(@RequestParam String village) {
        log.debug("Filtering customers by village={}", village);
        return ResponseEntity.ok(ApiResponse.ok("Customers", repo.findByVillageNameContainingIgnoreCase(village)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CustomerRequest request) {
        log.info("Updating customer id={}", id);
        Customer customer = repo.findById(id).orElseThrow();
        customer.setName(request.name());
        customer.setMobileNumber(request.mobileNumber());
        customer.setVillageName(request.villageName());
        Customer saved = repo.save(customer);
        log.info("Customer updated id={} village={}", saved.getId(), saved.getVillageName());
        return ResponseEntity.ok(ApiResponse.ok("Customer updated", saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.warn("Deleting customer id={}", id);
        repo.deleteById(id);
        log.info("Customer deleted id={}", id);
        return ResponseEntity.ok(ApiResponse.ok("Customer deleted", null));
    }
}
