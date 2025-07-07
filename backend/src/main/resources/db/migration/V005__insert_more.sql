INSERT INTO classes (school_id, name)
VALUES (1, '10A'),
       (1, '9C');

UPDATE users SET class_id = 2 WHERE id = 5;
UPDATE users SET class_id = 1 WHERE id = 8;
UPDATE users SET class_id = 1 WHERE id = 10;
UPDATE users SET class_id = 2 WHERE id = 11;
UPDATE users SET class_id = 2 WHERE id = 12;

INSERT INTO votings (name, description, level_type, start_time, end_time, creator_id, count_all, target_id)
VALUES ('Some', 'More', 2, '1975-11-10 00:00:00', '2025-11-10 00:00:00',
        (SELECT id FROM users WHERE email = 'maria@example.com'), 0, 1),
       ('New', 'New', 1, '1995-12-10 00:00:00', '2025-11-10 00:00:00',
        (SELECT id FROM users WHERE email = 'bulakovskijvladislav@gmail.com'), 0,1);

INSERT INTO answers (name, voting_id, count)
VALUES ('Варіант A', (SELECT id FROM votings WHERE name = 'Some' LIMIT 1), 0),
('Варіант B', (SELECT id FROM votings WHERE name = 'Some' LIMIT 1), 0);

INSERT INTO answers (name, voting_id, count)
VALUES ('Варіант 1', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0),
('Варіант 2', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0),
('Варіант 3', (SELECT id FROM votings WHERE name = 'New' LIMIT 1), 0);

INSERT INTO voting_user (user_id, voting_id, answer_id)
VALUES ((SELECT id FROM users WHERE email = 'ivan.fr@example.com'),
        (SELECT id FROM votings WHERE name = 'New' LIMIT 1),
       (SELECT id FROM answers WHERE name = 'Варіант 2' AND voting_id = (SELECT id FROM votings WHERE name = 'New' LIMIT 1)) ),
((SELECT id FROM users WHERE email = 'kateryna.b@example.com'),
(SELECT id FROM votings WHERE name = 'New' LIMIT 1),
(SELECT id FROM answers WHERE name = 'Варіант 1' AND voting_id = (SELECT id FROM votings WHERE name = 'New' LIMIT 1))),
((SELECT id FROM users WHERE email = 'taras.sh@example.com'),
(SELECT id FROM votings WHERE name = 'New' LIMIT 1),
(SELECT id FROM answers WHERE name = 'Варіант 3' AND voting_id = (SELECT id FROM votings WHERE name = 'New' LIMIT 1)));

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



