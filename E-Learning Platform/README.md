# E-Learning Platform

## Overview

This is an E-Learning Platform developed in Java using Spring Boot. The platform allows users to register as Students or Instructors, manage courses, enroll in courses, take quizzes, and track progress. The application is designed to meet specific business requirements and includes a set of MVP features.

---

## II. BUSINESS REQUIREMENTS (Updated)

**BR1:** Users can register and authenticate with roles: Student, Instructor, Admin.
**BR2:** Admins can manage users (CRUD, assign roles, delete users with cascade on related data).
**BR3:** Instructors can create, update, publish, archive, and delete courses.
**BR4:** Courses have category and difficulty level; users can view all courses and course details.
**BR5:** Students can enroll/unenroll in published courses; enrollments are managed with cascade delete.
**BR6:** Students can view their enrolled courses.
**BR7:** Instructors can create quizzes for their courses, add/edit questions.
**BR8:** Students can submit quiz attempts; scores are calculated automatically.
**BR9:** Audit logs are recorded for critical actions (login, enrollment, quiz submission, user management).
**BR10:** All endpoints are protected by role-based access (Spring Security).
**BR11:** Business logic is covered by unit and integration tests.

---

## III. MVP FEATURES (Updated)

### User & Identity Management

- Registration, authentication, role assignment (Student, Instructor, Admin)
- Profile update, password change
- Admin CRUD for users, including role management and cascade delete

### Course Lifecycle Management

- Create, update, publish, archive, delete courses (Instructor/Admin)
- Assign category and difficulty level

### Course Discovery & Enrollment

- List published courses
- Enroll/unenroll in courses (Student)
- View enrolled courses

### Quiz & Assessment Management

- Create quizzes and questions (Instructor)
- Submit quiz attempts (Student)
- Automatic score calculation

### Auditing & Administration

- Record audit logs for key actions
- Admin access to users, courses, and logs

---

## Architecture & Technologies

- **Backend:** Java 21, Spring Boot 3.2.5, JPA/Hibernate
- **Database:** PostgreSQL (cascade delete for all FK relations)
- **Frontend:** Thymeleaf
- **API Docs:** Swagger/OpenAPI
- **Security:** Spring Security (role-based, CSRF, password encoding)
- **Testing:** JUnit 5, Mockito, MockMvc (unit & integration tests)
- **Build:** Maven

## Project Structure

- `model.entities` — JPA entities (User, Course, Enrollment, Quiz, etc.)
- `model.dto` — DTOs for API and UI
- `repository` — Spring Data JPA repositories
- `service` — Business logic (CRUD, cascade, validation)
- `controller` — REST controllers (role-based endpoints)
- `exception` — Custom exceptions and global handler
- `config` — Security and Swagger configuration
- `resources/templates` — Thymeleaf HTML templates (demo)
- `resources/static` — CSS, JS, images
- `test` — Unit and integration tests (full coverage)

## Key Endpoints

- `/api/users` — User management (CRUD, roles, admin only)
- `/api/courses` — Course management (CRUD, publish/archive, instructor/admin)
- `/api/enrollments` — Enrollment management (student only)
- `/api/quizzes` — Quiz management (CRUD, instructor only)
- `/api/audit-logs` — Audit logs (admin only)

## Running the Application

1. Configure PostgreSQL and set credentials in `application.properties`. Ensure the database/schema exist. For test data, run the initialization script:

```sh
psql -U <user> -d postgres -f init_elearning.sql
```

2. Build and run:

```sh
mvn clean install
mvn spring-boot:run
```

3. Access the web UI at `http://localhost:8080` and Swagger UI at `/swagger-ui.html`.

## Testing & Coverage

- Run all tests:
  ```sh
  mvn test
  ```
