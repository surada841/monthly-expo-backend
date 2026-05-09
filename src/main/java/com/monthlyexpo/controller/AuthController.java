package com.monthlyexpo.controller;

import com.monthlyexpo.dto.request.LoginRequest;
import com.monthlyexpo.dto.request.RegisterInitiateRequest;
import com.monthlyexpo.dto.request.RegisterVerifyRequest;
import com.monthlyexpo.dto.response.ApiResponse;
import com.monthlyexpo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService auth;

    @PostMapping("/register/initiate")
    public ResponseEntity<?> initiate(@Valid @RequestBody RegisterInitiateRequest request) {
        log.info("Registration OTP request received for mobile={}", request.mobile());
        String response = auth.initiate(request);
        log.info("Registration OTP request processed for mobile={}", request.mobile());
        return ResponseEntity.ok(ApiResponse.ok("success", response));
    }

    @PostMapping("/register/verify")
    public ResponseEntity<?> verify(@Valid @RequestBody RegisterVerifyRequest request) {
        log.info("Registration OTP verification request received for mobile={}", request.mobile());
        String response = auth.verify(request);
        log.info("Registration completed successfully for mobile={}", request.mobile());
        return ResponseEntity.ok(ApiResponse.ok("success", response));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        log.info("Password login request received for mobile={}", request.mobile());
        var response = auth.login(request);
        log.info("Password login successful for mobile={}", request.mobile());
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<?> resend(@RequestParam String mobile) {
        log.info("Registration OTP resend requested for mobile={}", mobile);
        String response = auth.resend(mobile);
        log.info("Registration OTP resend processed for mobile={}", mobile);
        return ResponseEntity.ok(ApiResponse.ok("success", response));
    }
}
