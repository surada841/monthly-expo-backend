package com.monthlyexpo.dto.response; import com.monthlyexpo.enums.Role; public record LoginResponse(String token, Long userId, String name, String mobile, Role role){}
