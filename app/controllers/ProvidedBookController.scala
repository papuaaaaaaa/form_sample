/**
 * Created by papua on 2015/01/07.
 */

package controllers

import models.{ProvidedBook, AmazonProductAdvertisting, Book, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

object ProvidedBookController extends Controller{

  def index = Action {
    Ok(views.html.common.index("provided books", User.providedBooks,
      routes.ProvidedBookController.delete, "/provided_books/form"))
  }

  def form = Action {
    Ok(views.html.search.form("search", routes.ProvidedBookController.search, "/provided_books"))
  }


  def search = Action.async { implicit request =>
    val form = Form("keyword" -> text)
    val keyword = form.bindFromRequest.get
    AmazonProductAdvertisting.search(keyword).map(res => {
      val items = AmazonProductAdvertisting.parseXmlForProvided(res.xml)
      Ok(views.html.search.result("provided book", items.toList, "/provided_books", routes.ProvidedBookController.register))
    })
  }

  def register = Action { implicit request =>
    val form = Form (
      mapping(
        "title" -> text,
        "author" -> text,
        "isbn" -> text,
        "imageUrl" -> text
      )((title, author, isbn, imageUrl) => ProvidedBook(title, author, isbn, imageUrl))
        ((book:Book) => Some(book.title, book.author, book.isbn, book.imageUrl))
    )
    val book = form.bindFromRequest.get
    if (book.save > 0) {
      Redirect("/provided_books")
    } else {
      Redirect("/")
    }
  }

  def delete = Action { implicit request =>
    val form = Form (
      mapping(
        "title" -> text,
        "author" -> text,
        "isbn" -> text,
        "imageUrl" -> text,
        "id" -> number,
        "userId" -> number
      )(ProvidedBook.apply)(ProvidedBook.unapply)
    )
    val book = form.bindFromRequest.get
    if (book.delete > 0) {
      Redirect("/provided_books")
    } else {
      Ok(views.html.common.error("can not delete provided book"))
    }
  }
}
