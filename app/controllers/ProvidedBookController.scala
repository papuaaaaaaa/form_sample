/**
 * Created by papua on 2015/01/07.
 */

package controllers

import models.User
import play.api.mvc._

object ProvidedBookController extends Controller{
  def index = Action {
    Ok(views.html.provided.index("provided books", User.providedBooks))
  }

  def add = Action {
    Ok(views.html.common.search_form("search provided books!!!"))
  }
}
