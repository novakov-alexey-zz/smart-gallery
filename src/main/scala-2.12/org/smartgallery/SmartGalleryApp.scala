package org.smartgallery


import java.io.IOException
import java.util

import clarifai2.api.request.ClarifaiRequest.Callback
import clarifai2.dto.model.output.ClarifaiOutput
import clarifai2.dto.prediction.Concept
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.StrictLogging

import scala.collection.JavaConverters._
import scala.concurrent.duration._

object SmartGalleryApp extends App with StrictLogging {
  val conf = ConfigFactory.load("application.conf")
  val clarifyClient = new ClarifyClient(conf)
  clarifyClient.predictDir("/Users/alexey/Dropbox/Camera Uploads", getCallback)

  private def getCallback = {
    val startTime = System.currentTimeMillis()
    new Callback[util.List[ClarifaiOutput[Concept]]] {
      override def onClarifaiResponseUnsuccessful(errorCode: Int) =
        logger.error(s"unsuccessful prediction", errorCode)

      override def onClarifaiResponseNetworkError(e: IOException) =
        logger.error(s"prediction failed", e)

      override def onClarifaiResponseSuccess(result: util.List[ClarifaiOutput[Concept]]) = {
        logger.debug(s"prediction took: ${(System.currentTimeMillis() - startTime).millis.toSeconds} seconds")
        logger.info(result.asScala.map(r => r.data().asScala.map(_.name()).mkString("[", ",", "]")).mkString("\n"))
      }
    }
  }
}
