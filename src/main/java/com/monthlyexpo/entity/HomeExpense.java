package com.monthlyexpo.entity;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.LocalDate;
@Entity @Table(name="home_expenses") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HomeExpense extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private LocalDate expenseDate; private String category; private BigDecimal amount; private String description; }
