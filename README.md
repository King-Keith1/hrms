# HRMS — Human Resource Management System

## What is this project?

HRMS is a backend REST API that digitises the core operations of a company's HR department. Instead of managing employees, attendance, leave, and payroll through spreadsheets or manual processes, HRMS provides a secure, role-based API that different types of users (admins, managers, and employees) can interact with based on their permissions.

The system solves the following real business problems:

- **Employee management** — HR admins and managers can onboard new employees and assign them to departments
- **Attendance tracking** — Employees clock in daily by marking their hours worked, including overtime
- **Leave management** — Employees submit leave requests which managers or admins can approve or reject
- **Payroll calculation** — The system automatically calculates gross pay based on actual attendance records and approved paid leave, with overtime paid at 1.5x the hourly rate

---

## Business Rules

### Roles
| Role | What they can do |
|------|-----------------|
| `ROLE_ADMIN` | Full access — manage all employees across all departments, approve leave, generate payroll |
| `ROLE_MANAGER` | Can manage employees within their own department only, approve leave, generate payroll |
| `ROLE_EMPLOYEE` | Can mark their own attendance and submit leave requests |

### Payroll Calculation
Gross pay is calculated as follows:

```
For each working day:
  Regular pay  = hourlyRate × hoursWorked
  Overtime pay = hourlyRate × overtimeHours × 1.5

For approved paid leave days:
  Leave pay = hourlyRate × standardHoursPerDay × leaveDays

Gross Pay = Sum of all regular pay + overtime pay + paid leave pay
```

Overtime is any hours worked beyond the employee's `standardHoursPerDay` (default 8 hours).

### Attendance Rules
- An employee can only mark attendance once per day
- Hours worked must be greater than 0

### Leave Rules
- End date cannot be before start date
- Leave can be either `PAID` or `UNPAID`
- Leave starts with `PENDING` status and must be approved by a manager or admin

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Language |
| Spring Boot 4 | Framework |
| Spring Security 7 | Authentication & Authorization |
| JWT (jjwt 0.11.5) | Stateless token-based auth |
| PostgreSQL | Database |
| Hibernate / JPA | ORM |
| springdoc-openapi 2.8.5 | Swagger UI & API docs |
| BCrypt | Password hashing |
| Maven | Build tool |

---

## Project Structure

```
src/main/java/com/company/hrms/
├── config/
│   └── OpenApiConfig.java             # Swagger/OpenAPI configuration
├── controller/
│   ├── AttendanceController.java      # Attendance endpoints
│   ├── AuthController.java            # Register & login endpoints
│   ├── EmployeeController.java        # Employee CRUD endpoints
│   ├── LeaveController.java           # Leave request endpoints
│   ├── PayrollController.java         # Payroll endpoints
│   └── SystemController.java          # System clock endpoints
├── dto/
│   ├── AuthResponse.java              # Token response
│   ├── CreateEmployeeRequest.java     # Employee creation payload
│   ├── LoginRequest.java              # Login payload
│   ├── PayslipResponse.java           # Payroll response
│   └── RegisterRequest.java           # Registration payload
├── entity/
│   ├── Attendance.java                # Daily attendance record
│   ├── Department.java                # Company department
│   ├── Employee.java                  # Employee profile
│   ├── LeaveRequest.java              # Leave request record
│   ├── LeaveStatus.java               # PENDING, APPROVED, REJECTED
│   ├── LeaveType.java                 # PAID, UNPAID
│   ├── Payroll.java                   # Monthly payroll record
│   ├── Permission.java                # Permission enum
│   ├── Role.java                      # ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE
│   └── User.java                      # System user account
├── exception/
│   └── GlobalExceptionHandler.java    # Centralised error responses
├── repository/
│   ├── AttendanceRepository.java
│   ├── DepartmentRepository.java
│   ├── EmployeeRepository.java
│   ├── LeaveRepository.java
│   ├── PayrollRepository.java
│   └── UserRepository.java
├── security/
│   ├── CustomUserDetailsService.java  # Loads user from DB for auth
│   ├── JwtAuthenticationFilter.java   # Validates JWT on each request
│   ├── JwtService.java                # Generates and parses JWT tokens
│   ├── RolePermissionMapper.java      # Maps roles to permissions
│   └── SecurityConfig.java            # Spring Security filter chain
└── service/
    ├── AttendanceService.java         # Attendance business logic
    ├── AuthService.java               # Register and login logic
    ├── EmployeeService.java           # Employee creation logic
    ├── LeaveService.java              # Leave request logic
    ├── PayrollService.java            # Payroll calculation logic
    └── SystemClockService.java        # Simulated system clock
```

---

## Setup & Running

### Prerequisites
- Java 17+
- PostgreSQL installed and running
- Maven

### 1. Clone the repository
```bash
git clone <repository-url>
cd hrms
```

### 2. Create the PostgreSQL database
```sql
CREATE DATABASE hrms;
```

### 3. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hrms
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### 4. Environment variables
For local development the application uses sensible defaults. For production set these:
```bash
JWT_SECRET=your-super-secret-key-minimum-32-characters-long
JWT_EXPIRATION=86400000
```

### 5. Run the application
```bash
mvn spring-boot:run
```

### 6. Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

The application will automatically create all database tables on first run and seed the departments from `data.sql`.

---

## API Endpoints

### Auth — Public
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user account |
| POST | `/auth/login` | Login and receive a JWT token |

### Employees — ADMIN, MANAGER
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/employees` | Create a new employee |
| GET | `/employees` | Get all employees |
| GET | `/employees/{id}` | Get employee by ID |
| GET | `/employees/department/{deptId}` | Get employees by department |

### Attendance
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/attendance/mark` | EMPLOYEE | Mark today's attendance |
| GET | `/attendance/employee/{id}` | ADMIN, MANAGER | Get attendance for current month |

### Leaves
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/leaves` | EMPLOYEE | Submit a leave request |
| POST | `/leaves/{id}/approve` | ADMIN, MANAGER | Approve a leave request |
| GET | `/leaves` | ADMIN, MANAGER | Get all leave requests |
| GET | `/leaves/my` | EMPLOYEE | Get my leave requests |

### Payroll
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/payroll/generate/{employeeId}` | ADMIN, MANAGER | Generate monthly payroll |
| GET | `/payroll` | ADMIN, MANAGER | Get all payroll records |
| GET | `/payroll/employee/{id}` | ADMIN, MANAGER | Get payroll by employee |

### System
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/system/next-day` | ADMIN, MANAGER | Advance system date by 1 day |

---

## Security

- All endpoints except `/auth/**`, `/swagger-ui/**`, and `/api-docs/**` require a valid Bearer token
- Tokens are signed with HMAC-SHA256 and expire after 24 hours by default
- Passwords are hashed with BCrypt
- The JWT secret should always be set as an environment variable in production — never hardcoded

---

## Database Schema

```
departments     → id, name
users           → id, username, password, role, department_id
employees       → id, full_name, employee_number, department_id, hourly_rate,
                  standard_hours_per_day, active, user_id
attendance      → id, employee_id, date, hours_worked, overtime_hours
leave_requests  → id, employee_id, start_date, end_date, type, status
payroll         → id, employee_id, payroll_month, gross_pay
```

---

## See Also

- `TESTING_FLOW.html` — Visual step-by-step guide for testing all endpoints
