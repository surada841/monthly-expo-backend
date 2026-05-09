package com.monthlyexpo.entity;
import com.monthlyexpo.enums.OtpPurpose; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="otp_verifications") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OtpVerification extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private String mobile; private String requestId; @Enumerated(EnumType.STRING) private OtpPurpose purpose; private int resendCount; }
