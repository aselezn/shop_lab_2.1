DROP DATABASE IF EXISTS shop_db;
CREATE DATABASE IF NOT EXISTS shop_db DEFAULT CHARACTER SET utf8mb4;

DROP TABLE IF EXISTS shop_db.products;
CREATE TABLE IF NOT EXISTS shop_db.products(
	id CHAR(7) NOT NULL PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    color VARCHAR(50),
    price VARCHAR(20) NOT NULL CHECK (price > 0),
    balance INT NOT NULL CHECK (balance >= 0),
    CONSTRAINT check_id_length CHECK (LENGTH(id) = 7)
);

DROP TABLE IF EXISTS shop_db.orders;
CREATE TABLE IF NOT EXISTS shop_db.orders(
	id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    create_date DATE NOT NULL,
    customer_fullname VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    email VARCHAR(50),
    customer_address VARCHAR(200) NOT NULL,
    status CHAR(1) CHECK (status in ('P', 'S', 'C')) NOT NULL,
    shipping_date DATE,
    CONSTRAINT shipping_date_status CHECK(status='S' AND shipping_date is not null OR status != 'S' AND shipping_date IS NULL)
);

DELIMITER //
CREATE TRIGGER update_shipping_date
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    IF NEW.status = 'S' THEN
        SET NEW.shipping_date = CURDATE();
    END IF;
END //
DELIMITER ;

DROP TABLE IF EXISTS shop_db.orders_items;
CREATE TABLE IF NOT EXISTS shop_db.orders_items(
	order_id BIGINT NOT NULL,
    product_id CHAR(7) NOT NULL,
    order_price INT NOT NULL CHECK (order_price > 0),
    quantity INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY fk_order_id (order_id) REFERENCES shop_db.orders (id),
    FOREIGN KEY fk_product_id (product_id) REFERENCES shop_db.products (id),
    CONSTRAINT orders_items_quantity_chk CHECK (quantity > 0),
    CONSTRAINT check_product_id_length CHECK (LENGTH(product_id) = 7)
);

INSERT INTO shop_db.products (id, product_name, color, price, balance)
VALUES
    (3251615, 'Стол кухонный', 'белый', 8000, 12),
    (3251616, 'Стол кухонный', null, 8000, 15),
    (3251617, 'Стул столовый "гусарский"', 'орех', 4000, 0),
    (3251619, 'Стул столовый с высокой спинкой', 'белый', 3500, 37),
    (3251620, 'Стул столовый с высокой спинкой', 'коричневый', 3500, 52);

INSERT INTO shop_db.orders (id, create_date, customer_fullname, phone, email, customer_address, status, shipping_date)
VALUES
    (1, '20.11.2020', 'Сергей Иванов', '(981)123-45-67', null, 'ул. Веденеева, 20-1-41', 'S', '29.11.2020'),
    (2, '22.11.2020', 'Алексей Комаров', '(921)001-22-33', null, 'пр. Пархоменко 51-2-123', 'S', '29.11.2020'),
    (3, '28.11.2020', 'Ирина Викторова', '(911)009-88-77', null, 'Тихорецкий пр. 21-21', 'P', null),
    (4, '03.12.2020', 'Павел Николаев', null, 'pasha_nick@mail.ru', 'ул. Хлопина 3-88', 'P', null),
    (5, '03.12.2020', 'Антонина Васильева', '(931)777-66-55', 'antvas66@gmail.com;', 'пр. Науки, 11-3-9', 'P', null),
    (6, '10.12.2020', 'Ирина Викторова', '(911)009-88-77', null, 'Тихорецкий пр. 21-21', 'P', null);

INSERT INTO shop_db.orders_items (order_id, product_id, order_price, quantity)
VALUES
    (1, 3251616, 7500, 1),
    (2, 3251615, 7500, 1),
    (3, 3251615, 8000, 1),
    (3, 3251617, 4000, 4),
    (4, 3251619, 3500, 2),
    (5, 3251615, 8000, 1),
    (5, 3251617, 4000, 4),
    (6, 3251617, 4000, 2);
