# --- !Ups
CREATE TABLE user_table (
  name  VARCHAR (30),
  mail  VARCHAR (30)
);


# --- !Downs
DROP TABLE user_table;