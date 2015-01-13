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
      val count = SQL(
        """
            INSERT INTO user_table(name, mail) VALUES({name}, {mail});
        """
      ).on('name -> name,
          'mail -> mail).executeUpdate()
      count
    }

//    DB.withTransaction { implicit conn =>
//      // このブロックの処理は1つのトランザクションとなります
//    }
  }
}