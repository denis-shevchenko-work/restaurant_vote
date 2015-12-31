
CREATE TABLE restaurant
(
  id bigserial NOT NULL,
  address character varying(255),
  name character varying(255) NOT NULL,
  CONSTRAINT restaurant_pkey PRIMARY KEY (id),
  CONSTRAINT uk_restauant_name UNIQUE (name)
);

CREATE TABLE dish
(
  id bigserial NOT NULL,
  name character varying(255) NOT NULL,
  CONSTRAINT dish_pkey PRIMARY KEY (id),
  CONSTRAINT uk_dish_name UNIQUE (name)
);

CREATE TABLE account
(
  id bigserial NOT NULL,
  name character varying(255),
  email character varying(255),
  login character varying(255) NOT NULL,
  password character varying(255),
  roles character varying(255),
  CONSTRAINT account_pkey PRIMARY KEY (id),
  CONSTRAINT uk_user_login UNIQUE (login)
);


CREATE TABLE menu
(
  id bigserial NOT NULL,
  date timestamp without time zone,
  restaurant_id bigint NOT NULL,
  CONSTRAINT menu_pkey PRIMARY KEY (id),
  CONSTRAINT fk_menu_restaurant FOREIGN KEY (restaurant_id)
  REFERENCES restaurant (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE menu_item
(
  id bigserial NOT NULL,
  price numeric(19,2),
  dish_id bigint,
  menu_id bigint NOT NULL,
  CONSTRAINT menu_item_pkey PRIMARY KEY (id),
  CONSTRAINT fk_menu_item_dish FOREIGN KEY (dish_id)
  REFERENCES dish (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_menu_item_menu FOREIGN KEY (menu_id)
  REFERENCES menu (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE user_vote
(
  id bigserial NOT NULL,
  date timestamp without time zone,
  menu_id bigint,
  user_id bigint,
  CONSTRAINT user_vote_pkey PRIMARY KEY (id),
  CONSTRAINT fk_user_vote_menu FOREIGN KEY (menu_id)
  REFERENCES menu (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_user_vote_account FOREIGN KEY (user_id)
  REFERENCES account (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT uk_menu_id_user_id_date UNIQUE (menu_id, user_id, date)
);
