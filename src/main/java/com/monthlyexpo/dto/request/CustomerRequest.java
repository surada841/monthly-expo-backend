package com.monthlyexpo.dto.request; import jakarta.validation.constraints.*; public record CustomerRequest(@NotBlank String name, String mobileNumber, @NotBlank String villageName){}
