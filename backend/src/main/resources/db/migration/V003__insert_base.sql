INSERT INTO "users" ("email", "keycloak_user_id", "role", "first_name", "last_name")
VALUES ('bulakovskijvladislav@gmail.com', '95c06363-2168-4108-bf5b-d76bf8a8e31b',
        'DIRECTOR', 'Bulakovskiy', 'Vladyslav'),
       ('!deleted-user!@deleted.com', '1234567890abcdABCD0987654321',
        'DELETED', 'Deleted', 'User');

INSERT INTO schools (name, director_id)
VALUES ('Greenwood High School', (SELECT id FROM users WHERE email = 'bulakovskijvladislav@gmail.com'));

UPDATE users
SET school_id = (SELECT id FROM schools WHERE name = 'Greenwood High School')
WHERE email IN ('bulakovskijvladislav@gmail.com');