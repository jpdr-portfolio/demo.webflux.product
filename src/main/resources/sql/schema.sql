--DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product (
  id int AUTO_INCREMENT primary key,
  name VARCHAR(200) NOT NULL,
  category_id int NOT NULL,
  retailer_id int NOT NULL,
  is_active BOOLEAN NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  deletion_date TIMESTAMP WITH TIME ZONE
);

--DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
  id int AUTO_INCREMENT primary key,
  name VARCHAR(200) NOT NULL,
  is_active BOOLEAN NOT NULL,
  creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
  deletion_date TIMESTAMP WITH TIME ZONE
);



INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Smartphones', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Smartphones');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Laptops', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Laptops');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Televisions', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Televisions');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Cameras', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Cameras');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Headphones', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Headphones');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Dresses', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Dresses');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Shoes', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Shoes');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Accessories', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Accessories');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Fruits', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Fruits');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Vegetables', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Vegetables');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Beverages', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Beverages');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Cleaning Supplies', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Cleaning Supplies');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Furniture', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Furniture');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Decor', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Decor');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Toys', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Toys');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Books', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Books');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Stationery', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Stationery');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Cosmetics', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Cosmetics');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Sports Gear', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Sports Gear');

INSERT INTO category (name, is_active, creation_date, deletion_date)
SELECT 'Watches', true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM category WHERE name = 'Watches');






























-- Smartphones
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Galaxy S21', (SELECT id FROM category WHERE name = 'Smartphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Galaxy S21');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'iPhone 13', (SELECT id FROM category WHERE name = 'Smartphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'iPhone 13');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Google Pixel 6', (SELECT id FROM category WHERE name = 'Smartphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Google Pixel 6');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'OnePlus 9', (SELECT id FROM category WHERE name = 'Smartphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'OnePlus 9');

-- Laptops
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'MacBook Air', (SELECT id FROM category WHERE name = 'Laptops'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'MacBook Air');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Dell XPS 13', (SELECT id FROM category WHERE name = 'Laptops'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Dell XPS 13');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'HP Spectre x360', (SELECT id FROM category WHERE name = 'Laptops'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'HP Spectre x360');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Lenovo ThinkPad X1', (SELECT id FROM category WHERE name = 'Laptops'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Lenovo ThinkPad X1');

-- Televisions
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'LG OLED TV', (SELECT id FROM category WHERE name = 'Televisions'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'LG OLED TV');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Samsung QLED TV', (SELECT id FROM category WHERE name = 'Televisions'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Samsung QLED TV');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sony Bravia', (SELECT id FROM category WHERE name = 'Televisions'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sony Bravia');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'TCL 6-Series', (SELECT id FROM category WHERE name = 'Televisions'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'TCL 6-Series');

-- Cameras
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Canon EOS R5', (SELECT id FROM category WHERE name = 'Cameras'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Canon EOS R5');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Nikon Z6', (SELECT id FROM category WHERE name = 'Cameras'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Nikon Z6');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sony A7 III', (SELECT id FROM category WHERE name = 'Cameras'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sony A7 III');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Fujifilm X-T4', (SELECT id FROM category WHERE name = 'Cameras'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Fujifilm X-T4');

-- Headphones
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Bose QuietComfort 35', (SELECT id FROM category WHERE name = 'Headphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Bose QuietComfort 35');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sony WH-1000XM4', (SELECT id FROM category WHERE name = 'Headphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sony WH-1000XM4');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Apple AirPods Pro', (SELECT id FROM category WHERE name = 'Headphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Apple AirPods Pro');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sennheiser HD 450BT', (SELECT id FROM category WHERE name = 'Headphones'), 1, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sennheiser HD 450BT');

-- Dresses
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Summer Floral Dress', (SELECT id FROM category WHERE name = 'Dresses'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Summer Floral Dress');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Elegant Evening Gown', (SELECT id FROM category WHERE name = 'Dresses'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Elegant Evening Gown');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Casual Maxi Dress', (SELECT id FROM category WHERE name = 'Dresses'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Casual Maxi Dress');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Chic Cocktail Dress', (SELECT id FROM category WHERE name = 'Dresses'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Chic Cocktail Dress');

-- Shoes
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Running Sneakers', (SELECT id FROM category WHERE name = 'Shoes'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Running Sneakers');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Leather Boots', (SELECT id FROM category WHERE name = 'Shoes'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Leather Boots');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sandals', (SELECT id FROM category WHERE name = 'Shoes'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sandals');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Formal Dress Shoes', (SELECT id FROM category WHERE name = 'Shoes'), 2, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Formal Dress Shoes');

-- Fruits
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Fresh Apples', (SELECT id FROM category WHERE name = 'Fruits'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Fresh Apples');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Bananas', (SELECT id FROM category WHERE name = 'Fruits'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Bananas');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Oranges', (SELECT id FROM category WHERE name = 'Fruits'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Oranges');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Grapes', (SELECT id FROM category WHERE name = 'Fruits'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Grapes');

-- Vegetables
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Organic Carrots', (SELECT id FROM category WHERE name = 'Vegetables'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Organic Carrots');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Spinach', (SELECT id FROM category WHERE name = 'Vegetables'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Spinach');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Bell Peppers', (SELECT id FROM category WHERE name = 'Vegetables'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Bell Peppers');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Broccoli', (SELECT id FROM category WHERE name = 'Vegetables'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Broccoli');

-- Beverages
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Orange Juice', (SELECT id FROM category WHERE name = 'Beverages'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Orange Juice');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Sparkling Water', (SELECT id FROM category WHERE name = 'Beverages'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Sparkling Water');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Coca-Cola', (SELECT id FROM category WHERE name = 'Beverages'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Coca-Cola');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Lemonade', (SELECT id FROM category WHERE name = 'Beverages'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Lemonade');

-- Cleaning Supplies
INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'All-Purpose Cleaner', (SELECT id FROM category WHERE name = 'Cleaning Supplies'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'All-Purpose Cleaner');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Dish Soap', (SELECT id FROM category WHERE name = 'Cleaning Supplies'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Dish Soap');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Laundry Detergent', (SELECT id FROM category WHERE name = 'Cleaning Supplies'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Laundry Detergent');

INSERT INTO product (name, category_id, retailer_id, is_active, creation_date, deletion_date)
SELECT 'Toilet Cleaner', (SELECT id FROM category WHERE name = 'Cleaning Supplies'), 3, true, CURRENT_TIMESTAMP, NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE name = 'Toilet Cleaner');
