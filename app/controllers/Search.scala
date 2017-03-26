package controllers

import com.google.inject.Inject
import play.api.mvc._
import service.Elasticsearch

import scala.concurrent.ExecutionContext.Implicits.global

class Search @Inject() (components: ControllerComponents) extends AbstractController(components) {
  def index = Action {
    Ok(views.html.index("Please type keywords in the field"))
  }

  def query(query: String) = Action.async {
    val photos = Elasticsearch.searchFor(query)
    val paths = photos.map(_.map(_.path).mkString(", "))
    paths.map(p => Ok(views.html.search(s"Photos: $p")))
  }
}
