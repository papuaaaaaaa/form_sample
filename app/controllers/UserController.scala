/**
 * Created by papua on 2015/01/09.
 */

package controllers

import controllers.ProvidedBookController._
import play.api.mvc.{Action, Controller}

object UserController extends Controller {
  def register = Action {
    Ok(views.html.user.form("User Registration Form"))
  }
}
