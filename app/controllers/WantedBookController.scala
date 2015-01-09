/**
 * Created by papua on 2015/01/07.
 */

package controllers

import models.AmazonProductAdvertisting
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

object WantedBookController extends Controller {
  def index = Action {
    Ok(views.html.wanted.index("form"))
  }

  def search = Action.async {
    AmazonProductAdvertisting.search("企業").map(res =>
      Ok(views.html.wanted.index(res.xml.toString()))
    )
  }
}
