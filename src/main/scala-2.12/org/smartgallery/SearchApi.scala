package org.smartgallery

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContextExecutor

trait SearchApi {
  implicit val system = ActorSystem("SearchApi")
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val searchRoute: Route =
    path("search") {
      get {
        parameter('query) { (query) =>
          val photosF = Elasticsearch.searchFor(query)
          onSuccess(photosF) { photos =>
            complete {
              val paths = photos.map(_.path).mkString(", ")
              HttpResponse(StatusCodes.OK, entity = paths)
            }
          }
        }
      }
    }
}
