package models

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by papua on 2015/01/13.
 */
case class Book(val title:String, val author:String, val isbn:String, val imageUrl:String,
                val tableName:String, val id:Int = -1, val userId:Int = -1) {


  def save = {
    DB.withConnection { implicit c =>
      SQL(
        "INSERT INTO " +
          tableName + "(user_id, title, author, isbn, image_url) " +
          "VALUES({userId}, {title}, {author}, {isbn}, {imageUrl});"
      ).on(
          'userId -> -1,
          'title -> title,
          'author -> author,
          'isbn -> isbn,
          'imageUrl -> imageUrl
        ).executeUpdate()
    }
  }

  def delete = {
    DB.withConnection { implicit c =>
      SQL(
        "DELETE FROM " + tableName +
          " WHERE id={id} " +
          "AND user_id={userId} " +
          "AND title={title} " +
          "AND author={author}" +
          "AND isbn={isbn} " +
          "AND image_url={imageUrl};"
      ).on(
          'id -> id,
          'userId -> userId,
          'title -> title,
          'author -> author,
          'isbn -> isbn,
          'imageUrl -> imageUrl
        ).executeUpdate()
    }
  }

}