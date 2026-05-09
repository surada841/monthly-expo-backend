package com.monthlyexpo.service.impl;

import com.monthlyexpo.dto.request.LoginRequest;
import com.monthlyexpo.dto.request.RegisterInitiateRequest;
import com.monthlyexpo.dto.request.RegisterVerifyRequest;
import com.monthlyexpo.dto.response.LoginResponse;
import com.monthlyexpo.entity.OtpVerification;
import com.monthlyexpo.entity.User;
import com.monthlyexpo.enums.OtpPurpose;
import com.monthlyexpo.enums.Role;
import com.monthlyexpo.repository.OtpVerificationRepository;
import com.monthlyexpo.repository.UserRepository;
import com.monthlyexpo.security.JwtService;
import com.monthlyexpo.service.AuthService;
import com.monthlyexpo.service.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository users;
    private final OtpVerificationRepository otps;
    private final OtpService otpService;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    // For production, replace this with Redis or a DB-backed temporary registration table.
    private final Map<String, RegisterInitiateRequest> pending = new ConcurrentHashMap<>();

    @Override
    public String initiate(RegisterInitiateRequest request) {
        log.debug("Checking duplicate registration for mobile={}", request.mobile());
        if (users.existsByMobile(request.mobile())) {
            log.warn("Duplicate registration attempt for mobile={}", request.mobile());
            throw new RuntimeException("Mobile already registered");
        }

        log.info("Sending registration OTP for mobile={}", request.mobile());
        String requestId = otpService.sendRegistrationOtp(request.mobile());

        otps.save(OtpVerification.builder()
                .mobile(request.mobile())
                .requestId(requestId)
                .purpose(OtpPurpose.REGISTER)
                .build());

        pending.put(request.mobile(), request);
        log.info("Registration OTP sent and pending registration stored for mobile={}", request.mobile());
        return "OTP sent to mobile";
    }

    @Override
    public String verify(RegisterVerifyRequest request) {
        log.info("Verifying registration OTP for mobile={}", request.mobile());
        boolean verified = otpService.verifyRegistrationOtp(request.mobile(), request.otp());
        if (!verified) {
            log.warn("Invalid registration OTP attempt for mobile={}", request.mobile());
            throw new RuntimeException("Invalid OTP");
        }

        RegisterInitiateRequest pendingRequest = pending.get(request.mobile());
        if (pendingRequest == null) {
            log.warn("Registration verification failed because pending data expired for mobile={}", request.mobile());
            throw new RuntimeException("Registration data expired. Initiate again");
        }

        User savedUser = users.save(User.builder()
                .name(pendingRequest.name())
                .mobile(pendingRequest.mobile())
                .email(pendingRequest.email())
                .passwordHash(encoder.encode(pendingRequest.password()))
                .mobileVerified(true)
                .role(Role.USER)
                .build());

        pending.remove(request.mobile());
        log.info("User registered successfully with userId={} mobile={}", savedUser.getId(), savedUser.getMobile());
        return "Registration completed";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.debug("Finding user by mobile={} for password login", request.mobile());
        User user = users.findByMobile(request.mobile())
                .orElseThrow(() -> {
                    log.warn("Login failed: user not found for mobile={}", request.mobile());
                    return new RuntimeException("Invalid mobile or password");
                });

        if (!user.isMobileVerified()) {
            log.warn("Login blocked: mobile not verified for userId={} mobile={}", user.getId(), user.getMobile());
            throw new RuntimeException("Mobile not verified");
        }

        if (!encoder.matches(request.password(), user.getPasswordHash())) {
            log.warn("Login failed: invalid password for userId={} mobile={}", user.getId(), user.getMobile());
            throw new RuntimeException("Invalid mobile or password");
        }

        String token = jwt.generate(user);
        log.info("JWT generated after successful login for userId={} mobile={}", user.getId(), user.getMobile());
        return new LoginResponse(token, user.getId(), user.getName(), user.getMobile(), user.getRole());
    }

    @Override
    public String resend(String mobile) {
        log.info("Resending registration OTP for mobile={}", mobile);
        String requestId = otpService.resendRegistrationOtp(mobile);

        otps.save(OtpVerification.builder()
                .mobile(mobile)
                .requestId(requestId)
                .purpose(OtpPurpose.REGISTER)
                .build());

        log.info("Registration OTP resent for mobile={}", mobile);
        return "OTP resent";
    }
}
