package com.monthlyexpo.entity;
import com.monthlyexpo.enums.PaymentMode; import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.LocalDate;
@Entity @Table(name="tractor_payments") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TractorPayment extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(optional=false) private TractorBooking booking; private LocalDate paymentDate; private BigDecimal amount; @Enumerated(EnumType.STRING) private PaymentMode paymentMode; private String notes; }
