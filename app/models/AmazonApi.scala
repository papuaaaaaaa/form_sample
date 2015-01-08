/**
 * Created by papua on 2015/01/07.
 */
package models

import play.api.Play.current
import play.api.libs.ws._

import scala.collection.JavaConversions._
import scala.collection.mutable.Map
import scala.concurrent.Future

object AmazonApi {
  def search(keywords:String):Future[WSResponse] = {
    val params = Map(
      "Service" -> "AWSECommerceService",
      "Operation" -> "ItemSearch",
      "Keywords" -> keywords,
      "SearchIndex" -> "Books"
    )
    val url = new SignedRequestsHelper().sign(params)
    println(url)
    println("---")
    val holder : WSRequestHolder = WS
      .url(url)
      .withRequestTimeout(10000)
      .withFollowRedirects(true)
    holder.get()
  }
}
