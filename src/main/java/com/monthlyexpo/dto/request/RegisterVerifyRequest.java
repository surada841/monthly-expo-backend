package com.monthlyexpo.dto.request; import jakarta.validation.constraints.*; public record RegisterVerifyRequest(@NotBlank String mobile,@NotBlank String otp){}
