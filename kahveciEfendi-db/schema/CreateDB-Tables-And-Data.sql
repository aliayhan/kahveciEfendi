-- SCHEMA CREATION

CREATE DATABASE kahveciefendi 
	CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
    
USE kahveciefendi;

CREATE TABLE users (
	user_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, 
	login_name VARCHAR(30) NOT NULL, 
    full_name VARCHAR(60) NOT NULL,
	email VARCHAR(60) NOT NULL, 
	password VARCHAR(60) NOT NULL, 
	PRIMARY KEY (user_id),  
	UNIQUE KEY user_login_uk (login_name),
    UNIQUE KEY user_email_uk (email))
ENGINE = InnoDB;

CREATE TABLE drinks (
	drink_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, 
	name VARCHAR(30) NOT NULL, 
    description VARCHAR(60), -- Nullable
	price FLOAT(8,2) NOT NULL,
	PRIMARY KEY (drink_id),  
    UNIQUE KEY drink_name_uk (name))
ENGINE = InnoDB;

CREATE TABLE drink_additions (
	drink_addition_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, 
	name VARCHAR(30) NOT NULL, 
    description VARCHAR(60), -- Nullable
	price FLOAT(8,2) NOT NULL,
	PRIMARY KEY (drink_addition_id),  
    UNIQUE KEY drink_name_uk (name))
ENGINE = InnoDB;

CREATE TABLE drink_drink_additions (
	drdr_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT, 
    drink_id INT(11) UNSIGNED NOT NULL,
    drink_addition_id INT(11) UNSIGNED NOT NULL,
    PRIMARY KEY (drdr_id),  
	UNIQUE KEY drdr_drink_drink_addition_uk (drink_id, drink_addition_id),
	CONSTRAINT drdr_drink_id_fk FOREIGN KEY (drink_id) REFERENCES drinks(drink_id) ON DELETE CASCADE,
    CONSTRAINT drdr_drink_addition_id_fk FOREIGN KEY (drink_addition_id) REFERENCES drink_additions(drink_addition_id) ON DELETE CASCADE)
ENGINE = InnoDB;

CREATE TABLE orders (
	order_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    date VARCHAR(7) NOT NULL, -- Simplified for demo purposes, its like 2016-01
    total_price FLOAT(8,2) NOT NULL,
    discount FLOAT(8,2) NOT NULL,
    discount_reason VARCHAR(60), -- Nullable
    total_price_with_discount FLOAT(8,2) NOT NULL, -- Additional field for report creation: Less JOINS needed
    user_id INT(11) UNSIGNED NOT NULL,
	PRIMARY KEY (order_id),
    CONSTRAINT order_drink_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE)
ENGINE = InnoDB;

CREATE TABLE ordered_drinks (
	ordered_drink_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    date VARCHAR(7) NOT NULL, -- Simplified for demo purposes, its like 2016-01
    total_price_with_amount_and_additions FLOAT(8,2) NOT NULL, -- Additional field for report creation: Less JOINS needed
    amount INT(8) NOT NULL,
    drink_name VARCHAR(30) NOT NULL, -- Copied value of table drinks because drink can be deleted without influence on report data
    drink_price FLOAT(8,2) NOT NULL, -- Copied value of table drinks because drink can be deleted without influence on report data
    drink_id INT(11) UNSIGNED,       -- Nullable because drink can be deleted
    order_id INT(11) UNSIGNED NOT NULL,
	PRIMARY KEY (ordered_drink_id),
    KEY (drink_name),
    CONSTRAINT ordered_drinks_drink_id_fk FOREIGN KEY (drink_id) REFERENCES drinks(drink_id),
    CONSTRAINT ordered_drinks_order_id_fk FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE)
ENGINE = InnoDB;

CREATE TABLE ordered_drink_additions (
	ordered_drink_addition_id INT(11) UNSIGNED NOT NULL AUTO_INCREMENT,
    date VARCHAR(7) NOT NULL, -- Simplified for demo purposes, its like 2016-01
    drink_addition_name VARCHAR(30) NOT NULL, -- Copied value of table drink_additions because drink can be deleted without influence on report data
    drink_addition_price FLOAT(8,2) NOT NULL, -- Copied value of table drink_additions because drink can be deleted without influence on report data
    drink_addition_id INT(11) UNSIGNED,       -- Nullable because drink addition can be deleted
    ordered_drink_id INT(11) UNSIGNED NOT NULL,
	PRIMARY KEY (ordered_drink_addition_id),
    KEY (drink_addition_name),
    CONSTRAINT ordered_drink_additions_drink_id_fk FOREIGN KEY (drink_addition_id) REFERENCES drink_additions(drink_addition_id),
    CONSTRAINT ordered_drink_additions_ordered_drink_id_fk FOREIGN KEY (ordered_drink_id) REFERENCES ordered_drinks(ordered_drink_id) ON DELETE CASCADE)
ENGINE = InnoDB;


-- TESTDATA CREATION

INSERT INTO users (login_name, full_name, email, password) VALUES ('hakan', 'Hakan Akmen', 'hakan@kahveciefendi.com', 'hakan');
INSERT INTO users (login_name, full_name, email, password) VALUES ('john', 'John Arslan', 'john@iluvcoffee.com', 'john');
INSERT INTO users (login_name, full_name, email, password) VALUES ('jessica', 'Jessica Parker', 'jessica@kahveciefendi.com', 'jessica');

INSERT INTO drinks (name, description, price) VALUES ('Filtre kahve', 'Nefis ve taze', 4.00);
INSERT INTO drinks (name, description, price) VALUES ('Latte', 'Italyen style', 5.00);
INSERT INTO drinks (name, description, price) VALUES ('Mocha', 'Sert ve iyi', 6.00);
INSERT INTO drinks (name, description, price) VALUES ('Çay', 'Bir klasik', 3.00);
INSERT INTO drinks (name, description, price) VALUES ('Türk kahvesi', 'Boğazda keyif', 5.00);

INSERT INTO drink_additions (name, description, price) VALUES ('Süt', 'Mutlu ineklerden', 2.00);
INSERT INTO drink_additions (name, description, price) VALUES ('Fındık şurubu', 'Leziz', 3.00);
INSERT INTO drink_additions (name, description, price) VALUES ('Çikolata sosu', 'Büyüklere-küçüklere', 5.00);
INSERT INTO drink_additions (name, description, price) VALUES ('Limon', 'Klasik', 2.00);

INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'));
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'));
    
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Latte'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'));
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Latte'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Çikolata sosu'));
    
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Mocha'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'));
    
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Çay'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'));
    
INSERT INTO drink_drink_additions (drink_id, drink_addition_id) VALUES (
	(SELECT drink_id FROM drinks WHERE name = 'Türk kahvesi'), 
	(SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'));

-- Fake data for demo purposes to generate reports on. -> Surley you can create live data by using the application.
-- Fake data for demo purposes to generate reports on
-- Fake data for demo purposes to generate reports on

-- 3 orders at month 01
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-01', 120, 0, 120, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-01', 80, 0, 80, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-01', 50, 0, 50, (SELECT user_id FROM users WHERE login_name = 'john'));

INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-02', 200, 0, 200, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-03', 210, 0, 210, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-04', 180, 0, 180, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-05', 300, 0, 300, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-06', 270, 0, 270, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-07', 200, 0, 200, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-08', 350, 0, 350, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-09', 400, 0, 400, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-10', 333, 0, 333, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-11', 300, 0, 300, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-12', 420, 0, 420, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2017-01', 350, 0, 350, (SELECT user_id FROM users WHERE login_name = 'john'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2017-02', 450, 0, 450, (SELECT user_id FROM users WHERE login_name = 'john'));

INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-01', 20, 0, 20, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-02', 50, 0, 50, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-03', 70, 0, 70, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-04', 120, 0, 120, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-05', 50, 0, 50, (SELECT user_id FROM users WHERE login_name = 'jessica'));

-- 3 orders at month 06
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-06', 240, 0, 240, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-06', 20, 0, 20, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-06', 240, 0, 240, (SELECT user_id FROM users WHERE login_name = 'jessica'));

INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-07', 210, 0, 210, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-08', 250, 0, 250, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-09', 200, 0, 200, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-10', 233, 0, 233, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-11', 270, 0, 270, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2016-12', 240, 0, 240, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2017-01', 220, 0, 220, (SELECT user_id FROM users WHERE login_name = 'jessica'));
INSERT INTO orders (date, total_price, discount, total_price_with_discount, user_id) VALUES ('2017-02', 320, 0, 320, (SELECT user_id FROM users WHERE login_name = 'jessica'));

-- Fake data for demo purposes to generate reports on; E.g. order of Cappuchino should be divided in x entries and not just 2000

INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 20000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-02', 40000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-03', 30000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-04', 100000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-05', 200000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-06', 200000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-07', 200000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-08', 30000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-09', 25000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-10', 20000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-11', 45000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-12', 50000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-01', 200000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-02', 300000, 1, 'Filtre kahve', 1, (SELECT drink_id FROM drinks WHERE name = 'Filtre kahve'), (SELECT order_id FROM orders LIMIT 1));

-- x orders at month 01
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 20, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 20, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 40, 2, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 20, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 20, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 2000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));

INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-02', 4000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-03', 3000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-04', 10000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-05', 20000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-06', 20000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-07', 20000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-08', 3000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-09', 2500, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-10', 2000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-11', 4500, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-12', 5000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-01', 20000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-02', 30000, 1, 'Latte', 1, (SELECT drink_id FROM drinks WHERE name = 'Latte'), (SELECT order_id FROM orders LIMIT 1));

INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-01', 10000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-02', 20000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-03', 15000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-04', 50000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-05', 100000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-06', 100000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-07', 100000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-08', 15000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-09', 12000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-10', 10000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-11', 22000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2016-12', 25000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-01', 100000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));
INSERT INTO ordered_drinks (date, total_price_with_amount_and_additions, amount, drink_name, drink_price, drink_id, order_id) VALUES
('2017-02', 150000, 1, 'Mocha', 1, (SELECT drink_id FROM drinks WHERE name = 'Mocha'), (SELECT order_id FROM orders LIMIT 1));

-- Fake data for demo purposes to generate reports on; E.g. every entry is on ordered_drink 1, etc.

-- x orders at month 01
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 2, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Süt', 1000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));

INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-02', 'Süt', 1500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-03', 'Süt', 2500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-04', 'Süt', 2700, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-05', 'Süt', 3000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-06', 'Süt', 4000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-07', 'Süt', 3000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-08', 'Süt', 3200, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-09', 'Süt', 3500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-10', 'Süt', 4400, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-11', 'Süt', 4500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-12', 'Süt', 5000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-01', 'Süt', 4000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-02', 'Süt', 3000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Süt'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));

INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Fındık şurubu', 2000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-02', 'Fındık şurubu', 2500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-03', 'Fındık şurubu', 3500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-04', 'Fındık şurubu', 3700, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-05', 'Fındık şurubu', 4000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-06', 'Fındık şurubu', 5000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-07', 'Fındık şurubu', 3000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-08', 'Fındık şurubu', 4200, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-09', 'Fındık şurubu', 3500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-10', 'Fındık şurubu', 5400, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-11', 'Fındık şurubu', 6500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-12', 'Fındık şurubu', 8000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-01', 'Fındık şurubu', 7000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-02', 'Fındık şurubu', 4000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Fındık şurubu'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));

INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-01', 'Limon', 3000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-02', 'Limon', 3500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-03', 'Limon', 4500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-04', 'Limon', 4700, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-05', 'Limon', 5000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-06', 'Limon', 6000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-07', 'Limon', 5000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-08', 'Limon', 7200, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-09', 'Limon', 7500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-10', 'Limon', 7400, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-11', 'Limon', 8500, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2016-12', 'Limon', 8000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-01', 'Limon', 7000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));
INSERT INTO ordered_drink_additions (date, drink_addition_name, drink_addition_price, drink_addition_id, ordered_drink_id) VALUES
('2017-02', 'Limon', 4000, (SELECT drink_addition_id FROM drink_additions WHERE name = 'Limon'), (SELECT ordered_drink_id FROM ordered_drinks LIMIT 1));