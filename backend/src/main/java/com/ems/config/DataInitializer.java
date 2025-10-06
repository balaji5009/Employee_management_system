package com.ems.config;

import com.ems.entity.User;
import com.ems.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            createDemoUsers();
        }
    }

    private void createDemoUsers() {
        // Create Admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setRole(User.Role.ADMIN);
        admin.setEnabled(true);
        userRepository.save(admin);

        // Create HR Manager
        User hrManager = new User();
        hrManager.setUsername("hr.manager");
        hrManager.setPassword(passwordEncoder.encode("password123"));
        hrManager.setRole(User.Role.HR);
        hrManager.setEnabled(true);
        userRepository.save(hrManager);

        // Create Employee users
        String[] employeeUsernames = {"john.doe", "jane.smith", "mike.johnson", "sarah.wilson", "david.brown"};
        
        for (String username : employeeUsernames) {
            User employee = new User();
            employee.setUsername(username);
            employee.setPassword(passwordEncoder.encode("password123"));
            employee.setRole(User.Role.EMPLOYEE);
            employee.setEnabled(true);
            userRepository.save(employee);
        }

        System.out.println("Demo users created successfully!");
        System.out.println("Demo Credentials:");
        System.out.println("Admin - Username: admin, Password: password123");
        System.out.println("HR Manager - Username: hr.manager, Password: password123");
        System.out.println("Employees - Username: john.doe/jane.smith/mike.johnson/sarah.wilson/david.brown, Password: password123");
    }
}