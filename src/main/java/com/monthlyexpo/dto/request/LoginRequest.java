package com.monthlyexpo.dto.request; import jakarta.validation.constraints.*; public record LoginRequest(@NotBlank String mobile,@NotBlank String password){}
