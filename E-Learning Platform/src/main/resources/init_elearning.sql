
-- Create schema for the e-learning platform

INSERT INTO role (name) VALUES ('STUDENT');
INSERT INTO role (name) VALUES ('INSTRUCTOR');
INSERT INTO role (name) VALUES ('ADMIN');

INSERT INTO category (name) VALUES ('Programming');
INSERT INTO category (name) VALUES ('Mathematics');
INSERT INTO category (name) VALUES ('Data Science');

INSERT INTO users (email, password, name) VALUES ('student@example.com', '$2a$10$BtxCN5hnP8sgn5t0517Rl.hPc4wZoXtWwdTdkjhaqr8utNMkXyLW2', 'Alice Johnson');
INSERT INTO users (email, password, name) VALUES ('instructor@example.com', '$2a$10$BtxCN5hnP8sgn5t0517Rl.hPc4wZoXtWwdTdkjhaqr8utNMkXyLW2', 'Dr. Robert Smith');
INSERT INTO users (email, password, name) VALUES ('admin@example.com', '$2a$10$BtxCN5hnP8sgn5t0517Rl.hPc4wZoXtWwdTdkjhaqr8utNMkXyLW2', 'Admin User');
INSERT INTO users (email, password, name) VALUES ('student2@example.com', '$2a$10$BtxCN5hnP8sgn5t0517Rl.hPc4wZoXtWwdTdkjhaqr8utNMkXyLW2', 'Maria Lopez');
INSERT INTO users (email, password, name) VALUES ('student3@example.com', '$2a$10$BtxCN5hnP8sgn5t0517Rl.hPc4wZoXtWwdTdkjhaqr8utNMkXyLW2', 'John Lee');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.email = 'student@example.com' AND r.name = 'STUDENT';
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.email = 'instructor@example.com' AND r.name = 'INSTRUCTOR';
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.email = 'admin@example.com' AND r.name = 'ADMIN';
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.email = 'student2@example.com' AND r.name = 'STUDENT';
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, role r WHERE u.email = 'student3@example.com' AND r.name = 'STUDENT';

INSERT INTO course (title, description, difficulty, category_id, instructor_id, status)
SELECT 'Java Masterclass', 'A comprehensive course covering Java fundamentals, OOP, advanced topics, concurrency, streams, and real-world projects.', 'ADVANCED',
	   (SELECT id FROM category WHERE name = 'Programming'),
	   (SELECT id FROM users WHERE email = 'instructor@example.com'),
	   'PUBLISHED';
INSERT INTO course (title, description, difficulty, category_id, instructor_id, status)
SELECT 'Algebra I', 'Basic Algebra for beginners, covering equations, inequalities, graphs, and word problems.', 'BEGINNER',
	   (SELECT id FROM category WHERE name = 'Mathematics'),
	   (SELECT id FROM users WHERE email = 'instructor@example.com'),
	   'PUBLISHED';
INSERT INTO course (title, description, difficulty, category_id, instructor_id, status)
SELECT 'Python for Data Science', 'Learn Python from scratch, with a focus on data analysis, visualization, and machine learning.', 'INTERMEDIATE',
	   (SELECT id FROM category WHERE name = 'Data Science'),
	   (SELECT id FROM users WHERE email = 'instructor@example.com'),
	   'PUBLISHED';
INSERT INTO course (title, description, difficulty, category_id, instructor_id, status)
SELECT 'Discrete Mathematics', 'Explore logic, set theory, combinatorics, graph theory, and proofs.', 'ADVANCED',
	   (SELECT id FROM category WHERE name = 'Mathematics'),
	   (SELECT id FROM users WHERE email = 'instructor@example.com'),
	   'PUBLISHED';

INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Java Basics', 'Introduction to Java, JVM, and basic syntax.', 1, (SELECT id FROM course WHERE title = 'Java Masterclass');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Java OOP', 'Object-Oriented Programming in Java.', 2, (SELECT id FROM course WHERE title = 'Java Masterclass');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Algebra Foundations', 'Understanding variables, expressions, and equations.', 1, (SELECT id FROM course WHERE title = 'Algebra I');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Algebra Graphs', 'Graphing linear equations and inequalities.', 2, (SELECT id FROM course WHERE title = 'Algebra I');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Python Basics', 'Python syntax, variables, data types, and basic operations.', 1, (SELECT id FROM course WHERE title = 'Python for Data Science');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Data Analysis with Pandas', 'Using Pandas for dataframes, cleaning, and analysis.', 2, (SELECT id FROM course WHERE title = 'Python for Data Science');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Logic and Proofs', 'Introduction to logic, truth tables, and basic proof techniques.', 1, (SELECT id FROM course WHERE title = 'Discrete Mathematics');
INSERT INTO lessons (title, content, lesson_order, course_id)
SELECT 'Graph Theory', 'Graphs, trees, and combinatorics in discrete mathematics.', 2, (SELECT id FROM course WHERE title = 'Discrete Mathematics');

INSERT INTO quizzes (title, lesson_id) SELECT 'Java Basics Quiz', id FROM lessons WHERE title = 'Java Basics';
INSERT INTO quizzes (title, lesson_id) SELECT 'Java OOP Quiz', id FROM lessons WHERE title = 'Java OOP';
INSERT INTO quizzes (title, lesson_id) SELECT 'Algebra Foundations Quiz', id FROM lessons WHERE title = 'Algebra Foundations';
INSERT INTO quizzes (title, lesson_id) SELECT 'Python Basics Quiz', id FROM lessons WHERE title = 'Python Basics';
INSERT INTO quizzes (title, lesson_id) SELECT 'Logic and Proofs Quiz', id FROM lessons WHERE title = 'Logic and Proofs';

INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'What is JVM?', id, 0 FROM quizzes WHERE title = 'Java Basics Quiz';
INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'Which keyword is used for inheritance in Java?', id, 1 FROM quizzes WHERE title = 'Java Basics Quiz';
INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'What is encapsulation?', id, 0 FROM quizzes WHERE title = 'Java OOP Quiz';
INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'Solve: 2x + 3 = 7. What is x?', id, 0 FROM quizzes WHERE title = 'Algebra Foundations Quiz';
INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'Which library is used for dataframes in Python?', id, 0 FROM quizzes WHERE title = 'Python Basics Quiz';
INSERT INTO questions (text, quiz_id, correct_option_index) SELECT 'What is a tautology in logic?', id, 0 FROM quizzes WHERE title = 'Logic and Proofs Quiz';

INSERT INTO enrollments (student_id, course_id, enrolled_at)
SELECT u.id, c.id, NOW() FROM users u, course c WHERE u.email = 'student@example.com' AND c.title = 'Java Masterclass';
INSERT INTO enrollments (student_id, course_id, enrolled_at)
SELECT u.id, c.id, NOW() FROM users u, course c WHERE u.email = 'student2@example.com' AND c.title = 'Java Masterclass';
INSERT INTO enrollments (student_id, course_id, enrolled_at)
SELECT u.id, c.id, NOW() FROM users u, course c WHERE u.email = 'student3@example.com' AND c.title = 'Python for Data Science';
INSERT INTO enrollments (student_id, course_id, enrolled_at)
SELECT u.id, c.id, NOW() FROM users u, course c WHERE u.email = 'student@example.com' AND c.title = 'Algebra I';
INSERT INTO enrollments (student_id, course_id, enrolled_at)
SELECT u.id, c.id, NOW() FROM users u, course c WHERE u.email = 'student2@example.com' AND c.title = 'Discrete Mathematics';

INSERT INTO audit_logs (user_id, action, details, timestamp)
SELECT u.id, 'LOGIN', 'Alice Johnson logged in', NOW() FROM users u WHERE u.email = 'student@example.com';
INSERT INTO audit_logs (user_id, action, details, timestamp)
SELECT u.id, 'ENROLL', 'Alice enrolled in Java Masterclass', NOW() FROM users u WHERE u.email = 'student@example.com';
INSERT INTO audit_logs (user_id, action, details, timestamp)
SELECT u.id, 'QUIZ_SUBMIT', 'Maria Lopez submitted Java Fundamentals Quiz', NOW() FROM users u WHERE u.email = 'student2@example.com';
INSERT INTO audit_logs (user_id, action, details, timestamp)
SELECT u.id, 'QUIZ_SUBMIT', 'John Lee submitted Python Data Analysis Quiz', NOW() FROM users u WHERE u.email = 'student3@example.com';
INSERT INTO audit_logs (user_id, action, details, timestamp)
SELECT u.id, 'COURSE_COMPLETE', 'Alice completed Algebra I', NOW() FROM users u WHERE u.email = 'student@example.com';
