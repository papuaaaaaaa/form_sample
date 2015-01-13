/**
 * Created by papua on 2015/01/07.
 */

package controllers

import models.AmazonProductAdvertisting
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

object WantedBookController extends Controller {
  def index = Action {
    Ok(views.html.common.form("form"))
  }

  def search = Action.async { implicit request =>
    val form = Form("keyword" -> text)
    val keyword = form.bindFromRequest.get
    AmazonProductAdvertisting.search(keyword).map(res => {
      val items = AmazonProductAdvertisting.parseXml(res.xml)
      Ok(views.html.wanted.index(keyword, items, res.xml.toString))
    })
  }
}
