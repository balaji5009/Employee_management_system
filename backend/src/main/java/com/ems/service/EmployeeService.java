package com.ems.service;

import com.ems.dto.EmployeeDto;
import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.entity.User;
import com.ems.repository.DepartmentRepository;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Optional<EmployeeDto> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDto);
    }
    
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = convertToEntity(employeeDto);
        
        // Create user account for employee
        if (employeeDto.getUsername() != null && !employeeDto.getUsername().isEmpty()) {
            User user = new User();
            user.setUsername(employeeDto.getUsername());
            user.setPassword(passwordEncoder.encode("password123")); // Default password
            user.setRole(User.Role.EMPLOYEE);
            user.setEnabled(true);
            
            user = userRepository.save(user);
            employee.setUser(user);
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDto(savedEmployee);
    }
    
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setDesignation(employeeDto.getDesignation());
        employee.setSalary(employeeDto.getSalary());
        employee.setJoinDate(employeeDto.getJoinDate());
        
        if (employeeDto.getStatus() != null) {
            employee.setStatus(Employee.Status.valueOf(employeeDto.getStatus()));
        }
        
        if (employeeDto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return convertToDto(updatedEmployee);
    }
    
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        // Soft delete - mark as terminated
        employee.setStatus(Employee.Status.TERMINATED);
        employeeRepository.save(employee);
    }
    
    public List<EmployeeDto> getEmployeesByDepartment(Long departmentId) {
        return employeeRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public long getActiveEmployeeCount() {
        return employeeRepository.countActiveEmployees();
    }
    
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = modelMapper.map(employee, EmployeeDto.class);
        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
            dto.setDepartmentName(employee.getDepartment().getName());
        }
        if (employee.getUser() != null) {
            dto.setUsername(employee.getUser().getUsername());
        }
        dto.setStatus(employee.getStatus().name());
        return dto;
    }
    
    private Employee convertToEntity(EmployeeDto dto) {
        Employee employee = modelMapper.map(dto, Employee.class);
        
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employee.setDepartment(department);
        }
        
        if (dto.getStatus() != null) {
            employee.setStatus(Employee.Status.valueOf(dto.getStatus()));
        } else {
            employee.setStatus(Employee.Status.ACTIVE);
        }
        
        return employee;
    }
}