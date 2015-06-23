package api


import akka.actor.{ActorRefFactory, ActorSystem}
import api.handler.WidgetHandler
import com.github.vonnagy.service.container.http.routing.RoutedEndpoints
import model._
import lib._
import spray.http.MediaTypes._
import com.twitter.bijection.twitter_util._
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config._
import net.liftweb.json._
import org.jboss.netty.util.CharsetUtil
import scala.util.Success
import scala.util.Failure
import spray.http.HttpHeaders.RawHeader


class WidgetService(implicit val system: ActorSystem,
                    actorRefFactory: ActorRefFactory) extends RoutedEndpoints with WidgetHandler with UtilBijections {

  // Import the default Json marshaller and un-marshaller
  implicit val marshaller = jsonMarshaller
  implicit val unmarshaller = jsonUnmarshaller[Widget]

  val conf = ConfigFactory.load()

  /*
  val index = conf.getString("elasticsearch.index")
  val indexType = conf.getString("elasticsearch.indexType")
  */

  val index = "index3"
  val indexType = "codigo_postal"


  val route = {


    pathPrefix("widgets") {
      get {
        // GET /widgets
        pathEnd {
          respondWithMediaType(`application/json`) {
            // Push the handling to another context so that we don't block
            getWidget()
          }
        } ~
          // GET /widgets/{id}
          path(IntNumber) { id =>
            respondWithMediaType(`application/json`) {
              // Push the handling to another context so that we don't block
              getWidget(Some(id))
            }
          }
      } ~
      // POST /widgets
      post {        
        // Simulate the creation of a widget. This call is handled in-line and not through the per-request handler.
        entity(as[Widget]) { widget =>
          respondWithMediaType(`application/json`) {

            respondWithHeaders(corsHeaders) { 
              // Push the handling to another context so that we don't block 
              val json = FinagleClient.stringToJson( widget.name )
              val futureScala = twitter2ScalaFuture.apply( FinagleClient.documentSearch( index, indexType, json ) )

              onComplete( futureScala ) {
                case Success(f) => 
                  //complete(s"The result was $value")
                  complete( parse( f.getContent.toString(CharsetUtil.UTF_8) ) )
                case Failure(ex) => 
                  complete(s"An error occurred: ${ex.getMessage}")
              }

            }

          }
        }
        
      } ~
      // PUT /widgets/{id}
      put {
        path(IntNumber) { id =>
          // Simulate the update of a product. This call is handled in-line and not through the per-request handler.
          entity(as[Widget]) { widget =>
            // Push the handling to another context so that we don't block
            updateWidget(id, widget)
          }
        }
      } ~
      // DELETE /widgets/{id}
      delete {
        path(IntNumber) { id =>
          // Delete the widget
          deleteWidget(id)
        }
      }

    }

  }


  val corsHeaders = List(
    RawHeader("Access-Control-Allow-Origin","*"), 
    RawHeader("Host", "http://localhost"),
    RawHeader("Origin", "http://localhost"),
    RawHeader("Allow", "*"),
    RawHeader("Access-Control-Request-Method", "PUT, GET, POST, DELETE, OPTIONS"),
    RawHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Accept-Encoding, Accept-Language, Host, Referer, User-Agent")
  )


}
