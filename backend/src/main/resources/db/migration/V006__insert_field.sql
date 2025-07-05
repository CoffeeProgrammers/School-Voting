ALTER TABLE petitions ADD COLUMN target_name VARCHAR(255);
insert into petitions(target_name) values ('Some'), ('some');
