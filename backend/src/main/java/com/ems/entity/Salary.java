package com.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "salaries", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"employee_id", "month", "year"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private int month; // 1-12
    
    @Column(nullable = false)
    private int year;
    
    @Column(name = "basic_pay", nullable = false, precision = 10, scale = 2)
    private BigDecimal basicPay;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal allowances = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;
    
    @Column(name = "net_pay", nullable = false, precision = 10, scale = 2)
    private BigDecimal netPay;
    
    @Column(name = "generated_date")
    private LocalDate generatedDate;
    
    @PrePersist
    @PreUpdate
    private void calculateNetPay() {
        this.netPay = basicPay.add(allowances).subtract(deductions);
        if (this.generatedDate == null) {
            this.generatedDate = LocalDate.now();
        }
    }
}