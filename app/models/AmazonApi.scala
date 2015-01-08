/**
 * Created by papua on 2015/01/07.
 */
package models

import play.api.libs.ws._
import scala.concurrent.Future
import play.Play
import play.api.Play.current

object AmazonApi {
  def search(keywords:String):Future[WSResponse] = {
    val holder : WSRequestHolder = WS
      .url("http://ecs.amazonaws.com/onca/xml")
      .withRequestTimeout(10000)
      .withQueryString({"Service" -> "AWSECommerceService"})
      .withQueryString({"AWSAccessKeyId" -> Play.application.configuration.getString("amazon.api.key")})
      .withQueryString({"Operation" -> "ItemSearch"})
      .withQueryString({"Keywords" -> keywords})
      .withQueryString({"SearchIndex" -> "Books"})
      .withFollowRedirects(true)
    holder.get()
  }
}
