INSERT INTO classes (school_id, name)
VALUES (1, '10A'),
       (1, '9C');

UPDATE users SET class_id = 2 WHERE id = 5;
UPDATE users SET class_id = 1 WHERE id = 8;
UPDATE users SET class_id = 1 WHERE id = 10;
UPDATE users SET class_id = 2 WHERE id = 11;
UPDATE users SET class_id = 2 WHERE id = 12;

INSERT INTO votings (name, description, level_type, start_time, end_time, creator_id, count_all, target_id, school_id)
VALUES ('Some', 'More', 1, '1975-11-10 00:00:00', '2025-11-10 00:00:00',
        (SELECT id FROM users WHERE email = 'maria@example.com'), 0, 1, 1),
       ('New', 'New', 0, '1995-12-10 00:00:00', '2025-11-10 00:00:00',
        (SELECT id FROM users WHERE email = 'bulakovskijvladislav@gmail.com'), 0, 1, 1);

INSERT INTO answers (name, voting_id, count)
VALUES ('Option A', (SELECT id FROM votings WHERE name = 'Some' LIMIT 1), 0),
('Option B', (SELECT id FROM votings WHERE name = 'Some' LIMIT 1), 0);

INSERT INTO answers (name, voting_id, count)
VALUES ('Option 1', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0),
('Option 2', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0),
('Option 3', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0);

INSERT INTO voting_user (user_id, voting_id, answer_id)
SELECT u.id,
       v.id,
       (SELECT id FROM answers WHERE name = 'Option B' AND voting_id = v.id)
FROM users u
JOIN votings v ON v.name = 'Some'
WHERE u.class_id = v.target_id
  AND NOT EXISTS (
    SELECT 1 FROM voting_user vu WHERE vu.user_id = u.id AND vu.voting_id = v.id
);


INSERT INTO voting_user (user_id, voting_id, answer_id)
SELECT u.id,
       v.id,
       (SELECT id FROM answers WHERE name = 'Option 1' AND voting_id = v.id)
FROM users u
JOIN votings v ON v.name = 'New'
WHERE u.school_id = v.school_id
  AND NOT EXISTS (
    SELECT 1 FROM voting_user vu WHERE vu.user_id = u.id AND vu.voting_id = v.id
);

UPDATE answers a
SET count = (
    SELECT COUNT(*)
    FROM voting_user vu
    WHERE vu.answer_id = a.id
);

INSERT INTO petitions
(name, description, end_time, level_type, creator_id, status, count, target_id, count_needed, creation_time, target_name)
VALUES ('Some',
        'new',
        '2045-12-10 00:00:00',
        0,
        (SELECT id FROM users WHERE email = 'oleh@example.com'),
        0,
        0,
        1,
        0,
        '2000-12-10 00:00:00',
        'New'),
       ('Some',
        'new',
        '2045-12-10 00:00:00',
        1,
        (SELECT id FROM users WHERE email = 'oleh@example.com'),
        0,
        0,
        1,
        0,
        '2000-12-10 00:00:00',
        'Some');



