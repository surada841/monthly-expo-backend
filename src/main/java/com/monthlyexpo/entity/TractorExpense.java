package com.monthlyexpo.entity;
import com.monthlyexpo.enums.TractorExpenseCategory; import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.LocalDate;
@Entity @Table(name="tractor_expenses") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TractorExpense extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private LocalDate expenseDate; @Enumerated(EnumType.STRING) private TractorExpenseCategory category; private BigDecimal amount; private String description; }
