CREATE TABLE IF NOT EXISTS app_user (
  user_id UUID NOT NULL,
  username varchar(20) NOT NULL UNIQUE,
  password varchar(109) NOT NULL,
  role user_type,
  PRIMARY KEY (user_id)
);
