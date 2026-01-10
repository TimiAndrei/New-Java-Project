-- Delete all data from the E-Learning Platform tables in the correct order to avoid foreign key constraint errors.
-- This script does NOT drop tables or sequences, only deletes data.

-- Disable referential integrity temporarily if needed (optional, for advanced use)
-- SET session_replication_role = replica;

DELETE FROM audit_logs;
DELETE FROM enrollments;
DELETE FROM questions;
DELETE FROM quizzes;
DELETE FROM lessons;
DELETE FROM course;
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM category;
DELETE FROM role;

-- Reset sequences (optional, if you want IDs to start from 1 again)
-- ALTER SEQUENCE role_id_seq RESTART WITH 1;
-- ALTER SEQUENCE category_id_seq RESTART WITH 1;
-- ALTER SEQUENCE users_id_seq RESTART WITH 1;
-- ALTER SEQUENCE course_id_seq RESTART WITH 1;
-- ALTER SEQUENCE lessons_id_seq RESTART WITH 1;
-- ALTER SEQUENCE quizzes_id_seq RESTART WITH 1;
-- ALTER SEQUENCE questions_id_seq RESTART WITH 1;
-- ALTER SEQUENCE enrollments_id_seq RESTART WITH 1;
-- ALTER SEQUENCE audit_logs_id_seq RESTART WITH 1;

-- Enable referential integrity again (optional)
-- SET session_replication_role = DEFAULT;
