# Employee Management System

A comprehensive full-stack web application for managing employees, departments, attendance, and payroll with role-based access control.

## 🚀 Features

### Core Functionality
- **Employee Management**: Complete CRUD operations for employee records
- **Department Management**: Organize employees into departments
- **Attendance Tracking**: Mark and monitor employee attendance with various status types
- **Salary Management**: Generate and manage employee salaries with allowances and deductions
- **PDF Payslip Generation**: Automatically generate downloadable payslips
- **Role-Based Access Control**: Different access levels for Admin, HR, and Employee roles

### User Roles
- **ADMIN**: Full system access including user management and system configuration
- **HR**: Employee, department, attendance, and salary management
- **EMPLOYEE**: View personal information, attendance records, and salary history

### Technical Features
- **Secure Authentication**: JWT-based authentication with role-based authorization
- **Responsive Design**: Modern UI built with React and Tailwind CSS
- **RESTful API**: Well-structured backend API with proper error handling
- **Database Integration**: MySQL database with JPA/Hibernate
- **PDF Generation**: Professional payslip generation using iText library

## 🛠️ Technology Stack

### Backend
- **Java 17** with Spring Boot 3.x
- **Spring Security** for authentication and authorization
- **Spring Data JPA** with Hibernate for database operations
- **MySQL** database
- **JWT** for token-based authentication
- **iText** for PDF generation
- **Maven** for dependency management

### Frontend
- **React 18** with modern hooks
- **Vite** for fast development and building
- **React Router** for client-side routing
- **Axios** for HTTP requests
- **Tailwind CSS** for styling
- **Lucide React** for icons
- **React Hot Toast** for notifications

## 📋 Prerequisites

Before running this application, make sure you have the following installed:

- **Java 17** or higher
- **Node.js 18** or higher
- **MySQL 8.0** or higher
- **Maven 3.6** or higher
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Employee-Management-system
```

### 2. Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE employee_management;
```

2. Update database configuration in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_management
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Backend Setup
```bash
# Navigate to backend directory
cd backend

# Install dependencies and run the application
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 4. Frontend Setup
```bash
# Navigate to frontend directory (from project root)
cd frontend

# Install dependencies
npm install

# Start the development server
npm run dev
```

The frontend will start on `http://localhost:3000`

### 5. Access the Application
1. Open your browser and navigate to `http://localhost:3000`
2. Use any of the demo credentials provided below to login
3. The application will automatically initialize with demo data on first run

## 🔐 Demo Credentials

The application comes with pre-configured demo users:

### Admin User
- **Username**: `admin`
- **Password**: `password123`
- **Role**: ADMIN

### HR Manager
- **Username**: `hr.manager`
- **Password**: `password123`
- **Role**: HR

### Employee Users
- **Username**: `john.doe` | **Password**: `password123` | **Role**: EMPLOYEE
- **Username**: `jane.smith` | **Password**: `password123` | **Role**: EMPLOYEE
- **Username**: `mike.johnson` | **Password**: `password123` | **Role**: EMPLOYEE
- **Username**: `sarah.wilson` | **Password**: `password123` | **Role**: EMPLOYEE
- **Username**: `david.brown` | **Password**: `password123` | **Role**: EMPLOYEE

## 📊 Database Schema

The application uses the following main entities:

- **Users**: Authentication and authorization
- **Employees**: Employee personal and professional information
- **Departments**: Organizational departments
- **Attendance**: Daily attendance records
- **Salaries**: Monthly salary information

## 🔧 Configuration

### Backend Configuration
Key configuration files:
- `application.properties`: Database and application settings
- `data.sql`: Demo data initialization
- `SecurityConfig.java`: Security and CORS configuration

### Frontend Configuration
Key configuration files:
- `vite.config.js`: Development server and build configuration
- `tailwind.config.js`: Tailwind CSS customization
- `AuthContext.jsx`: Authentication state management

## 📱 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/verify` - Token verification

### Employees
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### Departments
- `GET /api/departments` - Get all departments
- `POST /api/departments` - Create new department
- `PUT /api/departments/{id}` - Update department
- `DELETE /api/departments/{id}` - Delete department

### Attendance
- `GET /api/attendance` - Get attendance records
- `POST /api/attendance/mark` - Mark attendance
- `DELETE /api/attendance/{id}` - Delete attendance record

### Salaries
- `GET /api/salaries` - Get salary records
- `POST /api/salaries/generate` - Generate salary
- `PUT /api/salaries/{id}` - Update salary
- `DELETE /api/salaries/{id}` - Delete salary

### Payslips
- `GET /api/payslip/download/{employeeId}/{month}/{year}` - Download payslip PDF
- `GET /api/payslip/view/{employeeId}/{month}/{year}` - View payslip PDF

## 🎨 UI Features

### Responsive Design
- Mobile-first responsive design
- Modern and clean interface
- Intuitive navigation with role-based menus

### Interactive Components
- Real-time form validation
- Toast notifications for user feedback
- Modal dialogs for data entry
- Sortable and filterable tables
- Search functionality
- Enhanced login experience with loading indicators

### Dashboard Features
- Role-specific dashboards
- Key metrics and statistics
- Quick action buttons
- Recent activity summaries

## ✨ Recent Improvements

### Version 1.1 Updates
- **Fixed Employee Creation**: Resolved API endpoint mapping issues that prevented new employee creation
- **Enhanced Login Experience**: Added 1.5-second minimum loading time for better user feedback
- **Corrected Demo Credentials**: Updated frontend to display accurate demo user credentials
- **Improved Error Handling**: Better error messages and user feedback throughout the application
- **API Endpoint Standardization**: Ensured consistent REST API endpoint structure

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-Based Authorization**: Different access levels for different user types
- **CORS Configuration**: Proper cross-origin resource sharing setup
- **Password Encryption**: BCrypt password hashing
- **Protected Routes**: Frontend route protection based on user roles

## 🚀 Deployment

### Backend Deployment
1. Build the application:
```bash
mvn clean package
```

2. Run the JAR file:
```bash
java -jar target/employee-management-system-0.0.1-SNAPSHOT.jar
```

### Frontend Deployment
1. Build for production:
```bash
npm run build
```

2. Serve the `dist` folder using any static file server.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in `application.properties`
   - Ensure the database exists

2. **Employee Creation Not Working**
   - Ensure backend server is running on port 8080
   - Verify API endpoints are correctly mapped in `EmployeeController.java`
   - Check browser console for any JavaScript errors
   - Confirm departments are loading in the dropdown

3. **Frontend Build Errors**
   - Clear node_modules and reinstall: `rm -rf node_modules && npm install`
   - Check Node.js version compatibility

4. **CORS Issues**
   - Verify CORS configuration in `SecurityConfig.java`
   - Check frontend proxy configuration in `vite.config.js`

5. **Authentication Issues**
   - Clear browser localStorage
   - Check JWT token expiration
   - Verify user credentials
   - Ensure demo data has been initialized properly

## 📞 Support

For support and questions, please open an issue in the GitHub repository.

## 🎯 Future Enhancements

- Email notifications for important events
- Advanced reporting and analytics
- Employee self-service portal enhancements
- Mobile application
- Integration with external HR systems
- Advanced attendance tracking with biometric support
