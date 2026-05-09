package com.monthlyexpo.entity;
import com.monthlyexpo.enums.Role; import jakarta.persistence.*; import lombok.*;
@Entity @Table(name="users") @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User extends BaseEntity { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private String name; @Column(unique=true,nullable=false) private String mobile; @Column(unique=true) private String email; @Column(nullable=false) private String passwordHash; private boolean mobileVerified; @Enumerated(EnumType.STRING) private Role role; }
