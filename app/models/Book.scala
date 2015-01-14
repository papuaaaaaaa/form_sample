package models

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by papua on 2015/01/13.
 */
case class Book(val title:String, val author:String, val isbn:String, val imageUrl:String) {
  def save = {
    DB.withConnection { implicit c =>
      SQL(
        """
          INSERT INTO provided_book_table(user_id, title, author, isbn, image_url) VALUES({userId}, {title}, {author}, {isbn}, {imageUrl});
        """
      ).on(
          'userId -> -1,
          'title -> title,
          'author -> author,
          'isbn -> isbn,
          'imageUrl -> imageUrl
        ).executeUpdate()
    }

    def delete = {

    }
  }
}
