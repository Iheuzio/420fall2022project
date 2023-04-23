DROP TABLE ingredient;
DROP TABLE sales_discount;
DROP TABLE sales_price;
DROP TABLE sales;
DROP TABLE soup_type;
DROP TABLE inventory;
DROP TABLE soup;
DROP TABLE transfer;
DROP TABLE cart;
DROP TABLE customer;
DROP TABLE account;

-- ingredient definition

CREATE TABLE ingredient(
	ingredient_id VARCHAR2(20) PRIMARY KEY,
	name VARCHAR2(20)
);


-- sales definition
CREATE TABLE sales_discount(
	discount NUMBER (2,3) NOT NULL,
	coupon_id VARCHAR2(20) NOT NULL,
	FOREIGN KEY (coupon_id) REFERENCES sales(coupon_id) 
);

CREATE TABLE sales_price(
	price NUMBER(2,3) NOT NULL, 
	coupon_id VARCHAR2(20) NOT NULL,
	FOREIGN KEY (coupon_id) REFERENCES sales(coupon_id)
);

CREATE TABLE sales(
	coupon_id VARCHAR2(20) PRIMARY KEY,
	coupon_type VARCHAR2(20)
);


-- soup_type definition

CREATE TABLE soup_type (
	soup_name VARCHAR2(20) NOT NULL,
	soup_id VARCHAR2(20) NOT NULL PRIMARY KEY
);


-- inventory definition

CREATE TABLE inventory (
	ingredient_id VARCHAR2(20),
	cost NUMBER(2,3) NOT NULL,
	stock NUMBER,
	FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
);

-- soup definition

CREATE TABLE soup (
	soup_id VARCHAR2(20),
	ingredient_id VARCHAR2(20),
	cost NUMBER(2,3),
	FOREIGN KEY (soup_id) REFERENCES soup_type(soup_id),
	FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
);


-- transfer definition

CREATE TABLE transfer(
	transfer_id VARCHAR2(20),
	quantity NUMBER,
	ingredient_id VARCHAR2(20),
	FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id)
);


-- cart definition

CREATE TABLE cart(
	cart_id VARCHAR2(20) PRIMARY KEY,
	soup_id VARCHAR2(20) NOT NULL,
	quantity NUMBER,
	coupon_id VARCHAR2(20) NULL,
	FOREIGN KEY (soup_id) REFERENCES soup(soup_id),
	FOREIGN KEY (coupon_id) REFERENCES sales(coupon_id)
);


-- customer definition

CREATE TABLE customer(
	cart_id VARCHAR2(20) NOT NULL,
	account_id VARCHAR2(20) NOT NULL,
	name VARCHAR2(20) NOT NULL,
	email VARCHAR2(20) NOT NULL,
	FOREIGN KEY (account_id) REFERENCES account(account_id),
	FOREIGN KEY (cart_id) REFERENCES cart(cart_id)
);


-- account definition

CREATE TABLE account (
	account_id VARCHAR2(15) PRIMARY KEY,
	username  VARCHAR2(20) NOT NULL,
	password VARCHAR2(20) NOT NULL,
	status VARCHAR2(1) DEFAULT 1
);
