# --- !Ups
CREATE TABLE user_table (
  id    SERIAL NOT NULL PRIMARY KEY,
  name  TEXT,
  mail  TEXT
);


# --- !Downs
DROP TABLE user_table;