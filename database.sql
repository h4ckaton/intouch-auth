/*
 * Copyright (c) 2018. This file is subject to the terms and conditions defined in file 'LICENSE.txt' which is part of this source code package.
 */

-- Scheme

CREATE TABLE _user (
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR(255) NOT NULL UNIQUE,
	name VARCHAR(20) NOT NULL UNIQUE,
	password_hash VARCHAR(53) NOT NULL,
	enabled BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE device (
  id BIGSERIAL,
  user_id BIGINT REFERENCES "`user`"(id) ON UPDATE CASCADE,
  resource_secret VARCHAR(128) NOT NULL UNIQUE,
  refresh_secret VARCHAR(128) NOT NULL UNIQUE,
  CONSTRAINT device_pk PRIMARY KEY (id, user_id)
)

CREATE TABLE role (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE user_role (
	user_id BIGINT REFERENCES _user(id) ON UPDATE CASCADE,
	role_id BIGINT REFERENCES role(id) ON UPDATE CASCADE,
	CONSTRAINT user_role_pk PRIMARY KEY (user_id, role_id)
);

CREATE TABLE privilege (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE role_privilege (
	privilege_id BIGINT REFERENCES privilege(id) ON UPDATE CASCADE,
	role_id BIGINT REFERENCES role(id) ON UPDATE CASCADE,
	CONSTRAINT privilege_role_pk PRIMARY KEY (privilege_id, role_id)
);


-- Data

INSERT INTO role (id, name) VALUES
(1, 'ROLE_USER');

INSERT INTO privilege (id, name) VALUES
(1, 'ADD_PRODUCT_PRIVILEGE'),
(2, 'RATE_PRODUCT_PRIVILEGE');

INSERT INTO role_privilege (role_id, privilege_id) VALUES
(1, 1),
(1, 2);
