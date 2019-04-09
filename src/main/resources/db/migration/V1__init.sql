CREATE TABLE rooms(
  id BIGINT NOT NULL AUTO_INCREMENT ,
  room_number BIGINT UNIQUE NOT NULL ,
  room_category VARCHAR(50) NOT NULL ,
  price BIGINT NOT NULL ,
  PRIMARY KEY (id),
);

CREATE TABLE users(
  id BIGINT NOT NULL AUTO_INCREMENT ,
  username VARCHAR(50) NOT NULL UNIQUE ,
  PRIMARY KEY (id),
);

CREATE TABLE additional_options(
  id BIGINT NOT NULL AUTO_INCREMENT ,
  option_type VARCHAR(50) NOT NULL UNIQUE ,
  price BIGINT NOT NULL ,
  PRIMARY KEY (id),
);

CREATE TABLE rooms_additional_options (
  room_id BIGINT NOT NULL,
  additional_option_id BIGINT NOT NULL
);

CREATE TABLE booked_rooms(
  id BIGINT NOT NULL AUTO_INCREMENT ,
  room_id BIGINT NOT NULL ,
  user_id BIGINT NOT NULL ,
  start_date TIMESTAMP NOT NULL ,
  end_date TIMESTAMP NOT NULL ,
  PRIMARY KEY (id),
  CONSTRAINT FK_booked_rooms_rooms FOREIGN KEY (room_id) REFERENCES rooms (id) ,
  CONSTRAINT FK_booked_rooms_users FOREIGN KEY (user_id) REFERENCES users (id)
);
