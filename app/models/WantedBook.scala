package models

/**
 * Created by papua on 2015/01/15.
 */
object WantedBook {
  def apply(title:String, author:String, isbn:String, imageUrl:String,
            id:Int = -1, userId:Int = -1):Book = {
    val tableName = TableNameConstant.WANTED_BOOK
    new Book(title, author, isbn, imageUrl, tableName, id, userId)
  }
}
