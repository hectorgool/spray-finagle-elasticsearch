package lib


import com.twitter.finagle.ServiceFactory
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.conversions.time._
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.util.CharsetUtil._
import org.jboss.netty.handler.codec.http.HttpHeaders.Names._
import org.jboss.netty.util.CharsetUtil
import com.twitter.util.Future
import net.liftweb.json._
import akka.event.slf4j.SLF4JLogging


object FinagleClient extends SLF4JLogging{


  val hosts = "localhost:9200"

  def stringToJson( term:String ): JValue = {

    val json = parse(""" {
      "size": 10,
      "query": {
        "match": {
          "_all": {
            "query": """ + "\"" +term +"\"" + """,
            "operator": "and"
          }
        }
      },
      "sort" : [
        {"colonia" : {"order" : "asc", "mode" : "avg"}}
      ]
    }""")

    json

  }

  val client: Service[HttpRequest, HttpResponse] = Http.newService(hosts)

  def requestBuilderGet(path: List[String], json: JValue): DefaultHttpRequest = {

    val payload = ChannelBuffers.copiedBuffer( compact(render(json)) , UTF_8)
    
    val _path = path.mkString("/","/","")
    
    val request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, _path)
    
    request.headers().add(USER_AGENT, "Finagle - Play")
    request.headers().add(HOST, "http://localhost")    
    request.headers().add(CONTENT_TYPE, "application/x-www-form-urlencoded")    
    request.headers().add(CONTENT_LENGTH, String.valueOf(payload.readableBytes()));
    request.setContent(payload)
    
    log.debug("Sending request:\n%s".format(request))
    
    log.debug("Sending body:\n%s".format(request.getContent.toString(CharsetUtil.UTF_8)))
    
    request


  }

  def sendToElastic(request: DefaultHttpRequest): Future[HttpResponse] = {
    log.debug("Request to send is %s" format request)
    val httpResponse = client(request)

    httpResponse.onSuccess{
      response =>
        log.debug("Received response: " + response)
    }.onFailure{ err: Throwable =>
        log.error(err.toString)
    }
  }

  def documentSearch(index: String, indexType: String, json: JValue): Future[HttpResponse] ={
    val req = requestBuilderGet(List( index, indexType, "_search"), json)
    sendToElastic(req)
  }


}