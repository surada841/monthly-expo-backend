package com.monthlyexpo.entity;
import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="customers") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private String name; private String mobileNumber; private String villageName; }
