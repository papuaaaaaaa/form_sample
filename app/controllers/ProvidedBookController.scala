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
  val form = Form (
    mapping(
      "title" -> text,
      "author" -> text,
      "isbn" -> text,
      "imageUrl" -> text
    )((title, author, isbn, imageUrl) => ProvidedBook(title, author, isbn, imageUrl))
      ((book:Book) => Some(book.title, book.author, book.isbn, book.imageUrl))
  )

  def index = Action {
    Ok(views.html.provided.index("provided books", User.providedBooks))
  }

  def add = Action {
    Ok(views.html.search.form("search provided books!!!", "/provided_books/search"))
  }


  def search = Action.async { implicit request =>
    val form = Form("keyword" -> text)
    val keyword = form.bindFromRequest.get
    AmazonProductAdvertisting.search(keyword).map(res => {
      val items = AmazonProductAdvertisting.parseXml(res.xml)
      Ok(views.html.wanted.index(keyword, items, res.xml.toString))
    })
  }

  def register = Action { implicit request =>
    val book = form.bindFromRequest.get
    if (book.save > 0) {
      Redirect("/provided_books")
    } else {
      Redirect("/")
    }
  }

  def delete = Action { implicit request =>
    val book = form.bindFromRequest.get
    if (book.delete > 0) {
      Redirect("/provided_books")
    } else {
      Redirect("/")
    }
  }
}
