package com.monthlyexpo.service.impl;

import com.monthlyexpo.service.OtpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Msg91OtpServiceImpl implements OtpService {

    @Override
    public String sendRegistrationOtp(String mobile) {
        // DEV MODE ONLY: Do not log real OTPs in production.
        log.info("DEV MODE: Sending registration OTP to mobile={}", mobile);
        log.debug("DEV MODE OTP for mobile={} is 123456", mobile);
        return "DEV_REQUEST_ID";
    }

    @Override
    public boolean verifyRegistrationOtp(String mobile, String otp) {
        log.info("Verifying registration OTP through DEV provider for mobile={}", mobile);
        boolean verified = "123456".equals(otp);
        if (!verified) {
            log.warn("DEV OTP verification failed for mobile={}", mobile);
        }
        return verified;
    }

    @Override
    public String resendRegistrationOtp(String mobile) {
        log.info("Resend registration OTP requested for mobile={}", mobile);
        return sendRegistrationOtp(mobile);
    }
}
