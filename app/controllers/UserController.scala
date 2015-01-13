/**
 * Created by papua on 2015/01/09.
 */

package controllers

import models.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{Action, Controller}

object UserController extends Controller {
  val form = Form (
    mapping(
      "name" -> text,
      "mail" -> text)(User.apply)(User.unapply)
  )

  def index = Action {
    val filledForm = form.fill(User("user name", "email address"))
    Ok(views.html.user.form(filledForm))
  }


  def register = Action { implicit request =>
    val user = form.bindFromRequest.get
    if (user.save > 0) {
      Ok(views.html.user.confirm("User registered name=\"" + user.name + "\" mail=\"" + user.mail + "\"."))
    } else {
      Ok(views.html.user.confirm("Error at save."))
    }
  }
}
