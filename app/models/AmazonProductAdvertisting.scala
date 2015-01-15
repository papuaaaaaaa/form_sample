/**
 * Created by papua on 2015/01/09.
 */

package models

import java.net.{URL, URLEncoder}
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64
import play.Play
import play.api.Play.current
import play.api.libs.ws.{WS, WSRequestHolder, WSResponse}

import scala.collection.SortedMap
import scala.concurrent.Future
import scala.xml.{Elem, Text}

object AmazonProductAdvertisting {

  private val method = "GET"

  private val dateFormatter = new ThreadLocal[SimpleDateFormat]() {
    override def initialValue():SimpleDateFormat = {
      val sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
      sdf.setTimeZone(TimeZone.getTimeZone("GMT"))
      sdf
    }
  }

  private def urlencode(value:String):String = {
    URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~")
  }

  /**
   * APIに渡す Query Stringを生成する
   */
  private def createQueryString(accessKeyId:String, associateTag:Option[String],
                        operation:String, _args:Map[String,String]):String = {
    // 署名はパラメータをキー昇順に並べた文字列に対して行われなければならないため一度SortedMapに詰める
    val args = SortedMap.newBuilder[String,String]
    args ++= _args
    args += "AWSAccessKeyId" -> accessKeyId
    associateTag.foreach(args += "AssociateTag" -> _)
    args += "Service" -> "AWSECommerceService"
    args += "Timestamp" -> dateFormatter.get.format(new Date())
    args += "Operation" -> operation
    args.result.map { kv => "%s=%s".format(kv._1, urlencode(kv._2)) }.mkString("&")
  }

  /**
   * createQueryStringで生成されたQuery Stringを secretAccessKeyで署名したリクエスト用URL文字列を返す
   * endpointは各国Amazonで異なる
   */
  private def createRequestURL(endpoint:String, queryString:String, secretAccessKey:String):String = {
    // Calculate signature
    val endpointURL = new URL(endpoint)
    val stringToSign =
      "%s\n%s\n%s\n%s".format(method, endpointURL.getHost(), endpointURL.getPath(), queryString)
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(new SecretKeySpec(secretAccessKey.getBytes("UTF-8"), "HmacSHA256"))
    val signature = new String(Base64.encodeBase64(mac.doFinal(stringToSign.getBytes("UTF-8"))))

    // Append signature to the URL
    "%s?%s&Signature=%s".format(endpoint, queryString, urlencode(signature))
  }

  /**
   * 上二つの処理を一度にやるバージョン
   */
  private def createRequestURL(endpoint:String,
                       accessKeyId:String, associateTag:Option[String], secretAccessKey:String,
                       operation:String, args:Map[String,String]):String = {
    val queryString = createQueryString(accessKeyId, associateTag, operation, args)
    return createRequestURL(endpoint, queryString, secretAccessKey)
  }

  def search(keywords:String):Future[WSResponse] = {
    val (accessKeyId, associateTag, secretAccessKey) = (
      Play.application.configuration.getString("amazon.access.key"),
      Some("papuaaaaaaa-22"),  // or None
      Play.application.configuration.getString("amazon.secret.key"))

    val url = createRequestURL(
      "http://ecs.amazonaws.jp/onca/xml",
      accessKeyId, associateTag, secretAccessKey,
      "ItemSearch",
      Map(
        "Keywords"->keywords,
        "SearchIndex"->"Books",
        "ResponseGroup"->"Medium"))
    //"Service" -> "AWSECommerceService",

    val holder : WSRequestHolder = WS
      .url(url)
      .withRequestTimeout(10000)
      .withFollowRedirects(true)
    holder.get()
  }

  def parseXmlForWanted(response:Elem):Seq[Book] = {
    for (
      item <- (response \\ "Item")
    ) yield {
      val attribute = item \ "ItemAttributes"

      val title = attribute \ "Title"
      val author = attribute \ "Author"
      val isbn = attribute \ "ISBN"
      val imageUrl = (item \\ "ImageSet").filter(_ \ "@Category" contains Text("variant")) \ "MediumImage" \ "URL"
      WantedBook(title.text, author.text, isbn.text, imageUrl.text)
    }
  }

  def parseXmlForProvided(response:Elem):Seq[Book] = {
    for (
      item <- (response \\ "Item")
    ) yield {
      val attribute = item \ "ItemAttributes"

      val title = attribute \ "Title"
      val author = attribute \ "Author"
      val isbn = attribute \ "ISBN"
      val imageUrl = (item \\ "ImageSet").filter(_ \ "@Category" contains Text("variant")) \ "MediumImage" \ "URL"
      ProvidedBook(title.text, author.text, isbn.text, imageUrl.text)
    }
  }
}
