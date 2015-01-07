/**
 * Created by papua on 2015/01/07.
 */

package controllers

import play.api.mvc._

object WantedBookController extends Controller {
  def index = Action {
    Ok("called index in wanted book controller")
  }
}
