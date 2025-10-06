package com.ems.controller;

import com.ems.dto.JwtResponse;
import com.ems.dto.LoginRequest;
import com.ems.dto.SignupRequest;
import com.ems.entity.Employee;
import com.ems.entity.User;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.UserRepository;
import com.ems.security.JwtUtils;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    EmployeeRepository employeeRepository;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User userPrincipal = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userPrincipal);
        
        // Get employee ID if exists
        Long employeeId = null;
        Optional<Employee> employee = employeeRepository.findByUserId(userPrincipal.getId());
        if (employee.isPresent()) {
            employeeId = employee.get().getId();
        }
        
        return ResponseEntity.ok(new JwtResponse(jwt,
                userPrincipal.getUsername(),
                userPrincipal.getRole().name(),
                employeeId));
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body("Error: Username is already taken!");
        }
        
        // Create new user's account
        User user = new User(null,
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getRole(),
                true,
                null);
        
        userRepository.save(user);
        
        return ResponseEntity.ok("User registered successfully!");
    }
}