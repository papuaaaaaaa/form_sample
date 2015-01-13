# --- !Ups
CREATE TABLE provided_book_table (
  id SERIAL NOT NULL PRIMARY KEY,
  user_id  INT,
  title TEXT,
  author TEXT,
  isbn TEXT,
  image_url TEXT
);

# --- !Downs
DROP TABLE provided_book_table;