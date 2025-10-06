package com.ems.service;

import com.ems.entity.Employee;
import com.ems.entity.Salary;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SalaryService {
    
    @Autowired
    private SalaryRepository salaryRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }
    
    public List<Salary> getSalariesByEmployee(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId);
    }
    
    public Optional<Salary> getSalaryByEmployeeAndMonth(Long employeeId, int month, int year) {
        return salaryRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year);
    }
    
    public Salary generateSalary(Long employeeId, int month, int year, BigDecimal allowances, BigDecimal deductions) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + employeeId));
        
        // Check if salary already exists for this employee and month/year
        Optional<Salary> existingSalary = salaryRepository.findByEmployeeIdAndMonthAndYear(employeeId, month, year);
        
        Salary salary;
        if (existingSalary.isPresent()) {
            // Update existing salary
            salary = existingSalary.get();
            salary.setAllowances(allowances != null ? allowances : BigDecimal.ZERO);
            salary.setDeductions(deductions != null ? deductions : BigDecimal.ZERO);
        } else {
            // Create new salary record
            salary = new Salary();
            salary.setEmployee(employee);
            salary.setMonth(month);
            salary.setYear(year);
            salary.setBasicPay(employee.getSalary()); // Use employee's base salary
            salary.setAllowances(allowances != null ? allowances : BigDecimal.ZERO);
            salary.setDeductions(deductions != null ? deductions : BigDecimal.ZERO);
            salary.setGeneratedDate(LocalDate.now());
        }
        
        return salaryRepository.save(salary);
    }
    
    public List<Salary> getSalariesByMonth(int month, int year) {
        return salaryRepository.findByMonthAndYear(month, year);
    }
    
    public void deleteSalary(Long id) {
        salaryRepository.deleteById(id);
    }
    
    public Salary updateSalary(Long id, BigDecimal allowances, BigDecimal deductions) {
        Salary salary = salaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salary record not found with id: " + id));
        
        salary.setAllowances(allowances != null ? allowances : BigDecimal.ZERO);
        salary.setDeductions(deductions != null ? deductions : BigDecimal.ZERO);
        
        return salaryRepository.save(salary);
    }
}