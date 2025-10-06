package com.ems.controller;

import com.ems.entity.Salary;
import com.ems.service.PdfService;
import com.ems.service.SalaryService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class PayslipController {
    
    @Autowired
    private SalaryService salaryService;
    
    @Autowired
    private PdfService pdfService;
    
    @GetMapping("/download/{employeeId}/{month}/{year}")
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable Long employeeId,
            @PathVariable int month,
            @PathVariable int year) {
        try {
            Optional<Salary> salaryOpt = salaryService.getSalaryByEmployeeAndMonth(employeeId, month, year);
            
            if (salaryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Salary salary = salaryOpt.get();
            byte[] pdfBytes = pdfService.generatePayslipPdf(salary);
            
            String filename = "payslip_" + salary.getEmployee().getName().replace(" ", "_") + 
                            "_" + month + "_" + year + ".pdf";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/view/{employeeId}/{month}/{year}")
    public ResponseEntity<byte[]> viewPayslip(
            @PathVariable Long employeeId,
            @PathVariable int month,
            @PathVariable int year) {
        try {
            Optional<Salary> salaryOpt = salaryService.getSalaryByEmployeeAndMonth(employeeId, month, year);
            
            if (salaryOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Salary salary = salaryOpt.get();
            byte[] pdfBytes = pdfService.generatePayslipPdf(salary);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(pdfBytes.length);
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}