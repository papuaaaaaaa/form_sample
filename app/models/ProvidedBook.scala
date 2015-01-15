package models

/**
 * Created by papua on 2015/01/15.
 */
object ProvidedBook {
  def apply(title:String, author:String, isbn:String, imageUrl:String,
           id:Int = -1, userId:Int = -1):Book = {
   val tableName = TableNameConstant.PROVIDED_BOOK
   new Book(title, author, isbn, imageUrl, tableName, id, userId)
  }

  def unapply(book:Book) = {
    //except tableName
    Option((book.title, book.author, book.isbn, book.imageUrl, book.id, book.userId))
  }
}
