CREATE TABLE IF NOT EXISTS app_user (
  user_id SERIAL NOT NULL,
  username varchar(20) NOT NULL,
  password varchar(104) NOT NULL,
  role user_type,
  PRIMARY KEY (user_id)
);