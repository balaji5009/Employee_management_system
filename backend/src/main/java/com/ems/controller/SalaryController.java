package com.ems.controller;

import com.ems.entity.Salary;
import com.ems.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/salary")
public class SalaryController {
    
    @Autowired
    private SalaryService salaryService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<Salary>> getAllSalaries() {
        List<Salary> salaries = salaryService.getAllSalaries();
        return ResponseEntity.ok(salaries);
    }
    
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Salary>> getSalariesByEmployee(@PathVariable Long employeeId) {
        List<Salary> salaries = salaryService.getSalariesByEmployee(employeeId);
        return ResponseEntity.ok(salaries);
    }
    
    @GetMapping("/employee/{employeeId}/month/{month}/year/{year}")
    public ResponseEntity<Salary> getSalaryByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @PathVariable int month,
            @PathVariable int year) {
        Optional<Salary> salary = salaryService.getSalaryByEmployeeAndMonth(employeeId, month, year);
        return salary.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Salary> generateSalary(@RequestBody Map<String, Object> request) {
        try {
            Long employeeId = Long.valueOf(request.get("employeeId").toString());
            int month = Integer.parseInt(request.get("month").toString());
            int year = Integer.parseInt(request.get("year").toString());
            
            BigDecimal allowances = request.get("allowances") != null ? 
                new BigDecimal(request.get("allowances").toString()) : BigDecimal.ZERO;
            BigDecimal deductions = request.get("deductions") != null ? 
                new BigDecimal(request.get("deductions").toString()) : BigDecimal.ZERO;
            
            Salary salary = salaryService.generateSalary(employeeId, month, year, allowances, deductions);
            return ResponseEntity.status(HttpStatus.CREATED).body(salary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/month/{month}/year/{year}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<List<Salary>> getSalariesByMonth(
            @PathVariable int month,
            @PathVariable int year) {
        List<Salary> salaries = salaryService.getSalariesByMonth(month, year);
        return ResponseEntity.ok(salaries);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<Salary> updateSalary(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        try {
            BigDecimal allowances = request.get("allowances") != null ? 
                new BigDecimal(request.get("allowances").toString()) : BigDecimal.ZERO;
            BigDecimal deductions = request.get("deductions") != null ? 
                new BigDecimal(request.get("deductions").toString()) : BigDecimal.ZERO;
            
            Salary salary = salaryService.updateSalary(id, allowances, deductions);
            return ResponseEntity.ok(salary);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSalary(@PathVariable Long id) {
        salaryService.deleteSalary(id);
        return ResponseEntity.noContent().build();
    }
}