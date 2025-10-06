package com.ems.service;

import com.ems.entity.Salary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {
    
    public byte[] generatePayslipPdf(Salary salary) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Title
            Paragraph title = new Paragraph("PAYSLIP")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18)
                    .setBold()
                    .setMarginBottom(20);
            document.add(title);
            
            // Employee Information Table
            Table empTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            
            empTable.addCell(createCell("Employee Name:", true));
            empTable.addCell(createCell(salary.getEmployee().getName(), false));
            
            empTable.addCell(createCell("Employee ID:", true));
            empTable.addCell(createCell(salary.getEmployee().getId().toString(), false));
            
            empTable.addCell(createCell("Department:", true));
            empTable.addCell(createCell(salary.getEmployee().getDepartment().getName(), false));
            
            empTable.addCell(createCell("Designation:", true));
            empTable.addCell(createCell(salary.getEmployee().getDesignation(), false));
            
            empTable.addCell(createCell("Pay Period:", true));
            empTable.addCell(createCell(getMonthName(salary.getMonth()) + " " + salary.getYear(), false));
            
            empTable.addCell(createCell("Generated Date:", true));
            empTable.addCell(createCell(salary.getGeneratedDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), false));
            
            document.add(empTable);
            
            // Salary Details Table
            Table salaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            
            // Header
            salaryTable.addCell(createHeaderCell("EARNINGS"));
            salaryTable.addCell(createHeaderCell("AMOUNT"));
            
            // Basic Pay
            salaryTable.addCell(createCell("Basic Pay", false));
            salaryTable.addCell(createCell("₹" + salary.getBasicPay().toString(), false));
            
            // Allowances
            salaryTable.addCell(createCell("Allowances", false));
            salaryTable.addCell(createCell("₹" + salary.getAllowances().toString(), false));
            
            // Gross Pay
            salaryTable.addCell(createCell("Gross Pay", true));
            salaryTable.addCell(createCell("₹" + salary.getBasicPay().add(salary.getAllowances()).toString(), true));
            
            document.add(salaryTable);
            
            // Deductions Table
            Table deductionTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);
            
            // Header
            deductionTable.addCell(createHeaderCell("DEDUCTIONS"));
            deductionTable.addCell(createHeaderCell("AMOUNT"));
            
            // Deductions
            deductionTable.addCell(createCell("Total Deductions", false));
            deductionTable.addCell(createCell("₹" + salary.getDeductions().toString(), false));
            
            document.add(deductionTable);
            
            // Net Pay Table
            Table netPayTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                    .setWidth(UnitValue.createPercentValue(100));
            
            netPayTable.addCell(createNetPayCell("NET PAY"));
            netPayTable.addCell(createNetPayCell("₹" + salary.getNetPay().toString()));
            
            document.add(netPayTable);
            
            document.close();
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
        
        return out.toByteArray();
    }
    
    private Cell createCell(String text, boolean isBold) {
        Cell cell = new Cell().add(new Paragraph(text));
        if (isBold) {
            cell.setBold();
        }
        cell.setPadding(8);
        return cell;
    }
    
    private Cell createHeaderCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(8);
    }
    
    private Cell createNetPayCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setBold()
                .setFontSize(14)
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(10);
    }
    
    private String getMonthName(int month) {
        String[] months = {"", "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        return months[month];
    }
}