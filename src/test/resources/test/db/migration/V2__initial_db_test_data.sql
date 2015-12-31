INSERT INTO account (name, email, login, password, roles) VALUES
('Admin Admin', 'admin@test.com', 'admin', '123', 'USER;ADMIN'),
('User User', 'user@test.com', 'user', '123', 'USER'),
('User2 User', 'user2@test.com', 'user2', '123', 'USER'),
('User3 User', 'user3@test.com', 'user3', '123', 'USER')
;

INSERT INTO restaurant ( name, address) VALUES
('Rest 1', ''),
('Rest 2', '');

INSERT INTO menu ( restaurant_id, date) VALUES
(1, now()),
(2, now());

INSERT INTO dish (name) VALUES
('Dish 1'),
('Dish 2'),
('Dish 3');


INSERT INTO menu_item ( menu_id, dish_id, price) VALUES
( 1, 1, 10.50),
( 1, 3, 11.20),
( 2, 2, 9.80),
( 2, 3, 11.60);