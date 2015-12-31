INSERT INTO account (id, name, email, login, password, roles) VALUES
(1, 'User User', 'user@test.com', 'user', '123', 'USER'),
(2, 'Admin Admin', 'admin@test.com', 'admin', '123', 'USER;ADMIN');

INSERT INTO restaurant (id, name, address) VALUES
(1, 'Rest 1', ''),
(2, 'Rest 2', '');

INSERT INTO dish (id, name) VALUES
(1, 'Dish 1'),
(2, 'Dish 2'),
(3, 'Dish 3');
