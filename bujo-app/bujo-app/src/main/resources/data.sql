-- Drop any existing demo user to avoid duplicates
DELETE FROM SHKVLNC.users WHERE email = 'demo@example1.com';

-- Insert demo user with BCrypt hash for "demo123"
INSERT INTO SHKVLNC.users (email, password, role)
VALUES (
  'demo@example1.com',
  '$2a$10$miWOQ97EQpY0oIdUjsaYkuAi0LTa6yqHcBhWu0Mh25hbkFJmeCVpe',
  'ROLE_USER'
);
