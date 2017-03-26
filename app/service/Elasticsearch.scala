package service


import com.sksamuel.elastic4s.http.{ElasticDsl, HttpClient}
import com.sksamuel.elastic4s.jackson.ElasticJackson.Implicits._
import com.sksamuel.elastic4s.{ElasticsearchClientUri, Hit, HitReader}
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import service.EsPhotoMapping._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Elasticsearch extends ElasticDsl {
  val host = "localhost"
  val port = 9200
  val client = HttpClient(ElasticsearchClientUri(host, port))

  private val indexAndType = indexName / `type`

  implicit object PhotoHitReader extends HitReader[Photo] {
    override def read(hit: Hit): Either[Throwable, Photo] = {
      Right(Photo(
        hit.sourceAsMap(filePath).toString,
        hit.sourceAsMap(conceptsList).toString.split(" ").toList))
    }
  }

  def insert(path: String, concepts: List[String]) =
    client.execute {
      indexInto(indexAndType)
        .fields(filePath -> path, conceptsList -> concepts.mkString(" "))
        .refresh(RefreshPolicy.IMMEDIATE)
    }

  def searchFor(concept: String): Future[List[Photo]] =
    client.execute {
      search(indexAndType) query termQuery(conceptsList, concept)
    } map (_.to[Photo].toList)
}

case class Photo(path: String, concepts: List[String])

object EsPhotoMapping {
  val indexName = "photos"
  val `type` = "mobile"
  val filePath = "filePath"
  val conceptsList = "concepts"
}