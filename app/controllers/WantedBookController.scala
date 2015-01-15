/**
 * Created by papua on 2015/01/07.
 */

package controllers

import models.{AmazonProductAdvertisting, Book, User, WantedBook}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object WantedBookController extends Controller {
  def index = Action {
    Ok(views.html.common.index("wanted books", User.wantedBooks))
  }

  def form = Action {
    Ok(views.html.search.form("search", "/wanted_books/search"))
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
    val form = Form (
      mapping(
        "title" -> text,
        "author" -> text,
        "isbn" -> text,
        "imageUrl" -> text
      )((title, author, isbn, imageUrl) => WantedBook(title, author, isbn, imageUrl))
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
      )(WantedBook.apply)(WantedBook.unapply)
    )
    val book = form.bindFromRequest.get
    if (book.delete > 0) {
      Redirect("/provided_books")
    } else {
      Ok(views.html.common.error("can not delete provided book"))
    }
  }
}
