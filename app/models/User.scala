package models

import play.api.db.DB
import anorm._
import play.api.Play.current

/**
 * Created by papua on 2015/01/09.
 */
case class User (name:String, mail:String) {
  def save = {
    DB.withConnection { implicit c =>
      SQL(
        """
          |INSERT INTO user_table(name, mail) VALUES({name}, {mail});
        """.stripMargin).on(
          'name -> name,
          'mail -> mail
        ).executeUpdate()
    }
  }

  def delete = {
    DB.withConnection { implicit c =>
      SQL(
        """
          |DELETE FROM user_table WHERE name={name} AND mail={mail};
        """
      ).on(
          'name -> name,
          'mail -> mail
        ).executeUpdate()
    }
  }

  def update = {
    DB.withConnection { implicit c =>
      SQL(
        """
          |UPDATE SET name={name}, mail={mail}
        """
      ).on(
          'name -> name,
          'mail -> mail
        ).executeUpdate()
    }
  }

  //    DB.withTransaction { implicit conn =>
  //      // このブロックの処理は1つのトランザクションとなります
  //    }
}