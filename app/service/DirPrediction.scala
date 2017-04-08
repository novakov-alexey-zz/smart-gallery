package service

import java.io.IOException
import java.util

import clarifai2.api.request.ClarifaiRequest.Callback
import clarifai2.dto.model.output.ClarifaiOutput
import clarifai2.dto.prediction.Concept
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging

import scala.collection.JavaConverters._
import scala.concurrent.duration._

object DirPrediction extends App with StrictLogging {
  private val conf = ConfigFactory.load("application.conf")
  val clarifai = new ClarifyService(conf)
  clarifai.predictDir("public/images", onClarifaiResponse)

  def onClarifaiResponse: String => Callback[util.List[ClarifaiOutput[Concept]]] = {
    fullPath: String => {
      val startTime = System.currentTimeMillis()
      new Callback[util.List[ClarifaiOutput[Concept]]] {
        override def onClarifaiResponseUnsuccessful(errorCode: Int) =
          logger.error(s"unsuccessful prediction", errorCode)

        override def onClarifaiResponseNetworkError(e: IOException) =
          logger.error(s"prediction failed", e)

        override def onClarifaiResponseSuccess(result: util.List[ClarifaiOutput[Concept]]) = {
          logger.debug(s"prediction took: ${(System.currentTimeMillis() - startTime).millis.toSeconds} seconds")
          val concepts = result.asScala.map(r => {
            logger.info(s"output: $r")
            r.data().asScala.map(_.name())
          }).head.toList
          logger.info(s"$fullPath: ${concepts.mkString(", ")}")
          Elasticsearch.insert(fullPath, concepts)
        }
      }
    }
  }
}
