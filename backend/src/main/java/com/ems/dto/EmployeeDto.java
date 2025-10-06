package com.ems.dto;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotNull(message = "Department is required")
    private Long departmentId;
    
    private String departmentName;
    
    @NotBlank(message = "Designation is required")
    private String designation;
    
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0")
    private BigDecimal salary;
    
    @NotNull(message = "Join date is required")
    private LocalDate joinDate;
    
    private String status;
    
    private String username;
}