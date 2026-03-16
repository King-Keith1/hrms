# HRMS — Human Resource Management System

A RESTful Spring Boot API for managing employees, attendance, leave requests, and payroll. Secured with JWT authentication and role-based access control.

---

## Tech Stack

- **Java 17**
- **Spring Boot 4**
- **Spring Security 7** with JWT
- **PostgreSQL**
- **Hibernate / JPA**
- **Swagger / OpenAPI 3** (springdoc)
- **Maven**

---

## Setup & Running

### Prerequisites
- Java 17+
- PostgreSQL running locally
- Maven

### 1. Create the database
```sql
CREATE DATABASE hrms;
```

### 2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hrms
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
```

### 3. Set environment variables (optional for local, required for production)
```bash
JWT_SECRET=your-super-secret-key-min-32-characters
JWT_EXPIRATION=86400000
```

### 4. Run the application
```bash
mvn spring-boot:run
```

### 5. Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## Roles

| Role           | Description                                      |
|----------------|--------------------------------------------------|
| `ROLE_ADMIN`   | Full access to all endpoints                     |
| `ROLE_MANAGER` | Can manage employees in their department         |
| `ROLE_EMPLOYEE`| Can mark attendance and request leave            |

---

## Testing Guide & Endpoint Flow

Follow these steps in order to test the full application flow.

---

### Step 1 — Register an Admin user

**POST** `/auth/register`

```json
{
  "username": "admin1",
  "password": "admin123",
  "role": "ADMIN",
  "departmentId": 1
}
```

> Copy the `token` from the response.

---

### Step 2 — Authorize in Swagger

Click the **Authorize** button (top right of Swagger UI).
Paste the token into the **bearerAuth** field and click **Authorize**.

---

### Step 3 — Register an Employee user

**POST** `/auth/register`

```json
{
  "username": "johndoe",
  "password": "john123",
  "role": "EMPLOYEE",
  "departmentId": 1
}
```

---

### Step 4 — Register a Manager user

**POST** `/auth/register`

```json
{
  "username": "manager1",
  "password": "manager123",
  "role": "MANAGER",
  "departmentId": 1
}
```

---

### Step 5 — Login as Admin and authorize

**POST** `/auth/login`

```json
{
  "username": "admin1",
  "password": "admin123"
}
```

> Copy the token and re-authorize in Swagger with this fresh token.

---

### Step 6 — Create an Employee record (as Admin)

**POST** `/employees`

```json
{
  "fullName": "John Doe",
  "employeeNumber": "EMP001",
  "departmentId": 1,
  "hourlyRate": 50.00,
  "username": "johndoe"
}
```

> The `username` field automatically links the user account to the employee record.

---

### Step 7 — View all Employees (as Admin)

**GET** `/employees`

---

### Step 8 — View Employee by ID (as Admin)

**GET** `/employees/1`

---

### Step 9 — View Employees by Department (as Admin)

**GET** `/employees/department/1`

---

### Step 10 — Mark Attendance (as Employee)

> Switch to johndoe's token in Swagger Authorize first. (You can use this and login as johndoe - {"username": "johndoe",
"password": "john123"})

**POST** `/attendance/mark?hoursWorked=8`

---

### Step 11 — View Attendance (as Admin/Manager)

> Switch back to admin token.

**GET** `/attendance/employee/1`

---

### Step 12 — Request Leave (as Employee)

> Switch to johndoe's token.

**POST** `/leaves`

```
startDate: 2026-03-20
endDate:   2026-03-21
type:      PAID
```

---

### Step 13 — View My Leaves (as Employee)

**GET** `/leaves/my`

---

### Step 14 — Approve Leave (as Admin/Manager)

> Switch back to admin token.

**POST** `/leaves/{id}/approve`

> Use the `id` returned from Step 12.

---

### Step 15 — View All Leaves (as Admin/Manager)

**GET** `/leaves`

---

### Step 16 — Generate Payroll (as Admin/Manager)

**POST** `/payroll/generate/1?month=2026-03`

---

### Step 17 — View All Payroll (as Admin/Manager)

**GET** `/payroll`

---

### Step 18 — View Payroll by Employee (as Admin/Manager)

**GET** `/payroll/employee/1`

---

### Step 19 — Advance System Date (as Admin/Manager)

**POST** `/system/next-day`

> Useful for testing attendance across multiple days.

---

## Endpoint Summary

### Auth
| Method | Endpoint           | Access | Description          |
|--------|--------------------|--------|----------------------|
| POST   | `/auth/register`   | Public | Register a new user  |
| POST   | `/auth/login`      | Public | Login and get token  |

### Employees
| Method | Endpoint                          | Access          | Description                    |
|--------|-----------------------------------|-----------------|--------------------------------|
| POST   | `/employees`                      | ADMIN, MANAGER  | Create an employee             |
| GET    | `/employees`                      | ADMIN, MANAGER  | Get all employees              |
| GET    | `/employees/{id}`                 | ADMIN, MANAGER  | Get employee by ID             |
| GET    | `/employees/department/{deptId}`  | ADMIN, MANAGER  | Get employees by department    |

### Attendance
| Method | Endpoint                        | Access          | Description                        |
|--------|---------------------------------|-----------------|------------------------------------|
| POST   | `/attendance/mark`              | EMPLOYEE        | Mark today's attendance            |
| GET    | `/attendance/employee/{id}`     | ADMIN, MANAGER  | Get attendance for current month   |

### Leaves
| Method | Endpoint                | Access          | Description           |
|--------|-------------------------|-----------------|-----------------------|
| POST   | `/leaves`               | EMPLOYEE        | Request leave         |
| POST   | `/leaves/{id}/approve`  | ADMIN, MANAGER  | Approve a leave       |
| GET    | `/leaves`               | ADMIN, MANAGER  | Get all leave requests|
| GET    | `/leaves/my`            | EMPLOYEE        | Get my leave requests |

### Payroll
| Method | Endpoint                        | Access          | Description                  |
|--------|---------------------------------|-----------------|------------------------------|
| POST   | `/payroll/generate/{employeeId}`| ADMIN, MANAGER  | Generate payroll for a month |
| GET    | `/payroll`                      | ADMIN, MANAGER  | Get all payroll records      |
| GET    | `/payroll/employee/{id}`        | ADMIN, MANAGER  | Get payroll by employee      |

### System
| Method | Endpoint            | Access          | Description              |
|--------|---------------------|-----------------|--------------------------|
| POST   | `/system/next-day`  | ADMIN, MANAGER  | Advance system date by 1 |

---

## Security Notes

- All endpoints except `/auth/**`, `/swagger-ui/**`, and `/api-docs/**` require a valid JWT token
- Tokens expire after 24 hours (configurable via `JWT_EXPIRATION`)
- Passwords are hashed with BCrypt
- JWT secret should be set as an environment variable in production

---

## Project Structure

```
src/main/java/com/company/hrms/
├── config/          # OpenAPI config
├── controller/      # REST controllers
├── dto/             # Request/Response DTOs
├── entity/          # JPA entities
├── exception/       # Global exception handler
├── repository/      # Spring Data JPA repositories
├── security/        # JWT filter, JWT service, Security config
└── service/         # Business logic
```
