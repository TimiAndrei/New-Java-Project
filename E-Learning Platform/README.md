# E-Learning Platform

## Overview

This is an E-Learning Platform developed in Java using Spring Boot. The platform allows users to register as Students or Instructors, manage courses, enroll in courses, take quizzes, and track progress. The application is designed to meet specific business requirements and includes a set of MVP features.

---

## II. BUSINESS REQUIREMENTS

**BR1:** Users can register and authenticate with roles: Student, Instructor, Admin.

**BR2:** Users can manage their personal profile information.

**BR3:** Instructors can create, update, publish, and archive Courses.

**BR4:** Courses are classified by Category and Difficulty Level.

**BR5:** Students can browse and search courses by category, level, or instructor.

**BR6:** Students can enroll in and unenroll from published courses.

**BR7:** Instructors can create Quizzes associated with their courses.

**BR8:** Quizzes contain multiple Questions with predefined answers.

**BR9:** Students can submit Quiz Attempts, and the system automatically computes scores.

**BR10:** Administrators can view and manage users and courses.

**BR11:** The system records an Audit Log for critical user actions (login, enrollment, quiz submission).

---

## III. MVP FEATURES

### Feature A — User & Identity Management

- Registration, authentication, role assignment
- Profile validation and updates

### Feature B — Course Lifecycle Management

- Create, update, publish, archive courses
- Assign category and difficulty level

### Feature C — Course Discovery & Search

- List published courses
- Filter by category, level, instructor
- View course details

### Feature D — Enrollment Management

- Enroll / unenroll students from courses
- View enrolled courses

### Feature E — Quiz & Assessment Management

- Create quizzes and questions
- Submit quiz attempts
- Auto-calculate and store scores

### Feature F — Auditing & Administration

- Record audit logs
- Admin access to users, courses, and logs

---

## Architecture & Technologies

- **Backend:** 21.0.9, Spring Boot 3.2.5, JPA/Hibernate
- **Database:** PostgreSQL
- **Frontend:** Thymeleaf, Bootstrap 5
- **API Docs:** Swagger/OpenAPI
- **Security:** Spring Security (role-based, CSRF, password encoding)
- **Testing:** JUnit 5, Mockito, MockMvc (unit & integration tests)
- **Build:** Maven

## Project Structure

- `model.entities` — JPA entities (User, Course, Enrollment, Quiz, etc.)
- `model.dto` — DTOs for API and UI
- `repository` — Spring Data JPA repositories
- `service` — Business logic
- `controller` — REST controllers
- `exception` — Custom exceptions and global handler
- `config` — Security and Swagger configuration
- `resources/templates` — Thymeleaf HTML templates
- `resources/static` — CSS, JS, images
- `test` — Unit and integration tests

## Key Endpoints

- `/api/users` — User management
- `/api/courses` — Course management
- `/api/enrollments` — Enrollment management
- `/api/quizzes` — Quiz management
- `/api/audit-logs` — Audit logs

## Running the Application

1. Configure PostgreSQL and set credentials in `application.properties`. (Ensure the database and schema exist. For test data you can run the initialization script like this: psql -U <user> -d postgres -f init_elearning.sql)
2. Build and run:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
3. Access the web UI at `http://localhost:8080` and Swagger UI at `/swagger-ui.html`.

## Testing

- Run all tests:
  ```sh
  mvn test
  ```
- All business logic and endpoints are covered by unit and integration tests.
