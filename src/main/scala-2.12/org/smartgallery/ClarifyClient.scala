package org.smartgallery

import java.io.File
import java.util
import java.util.concurrent.TimeUnit

import clarifai2.api.ClarifaiBuilder
import clarifai2.api.request.ClarifaiRequest.Callback
import clarifai2.dto.input.ClarifaiInput
import clarifai2.dto.input.image.ClarifaiImage
import clarifai2.dto.model.output.ClarifaiOutput
import clarifai2.dto.prediction.Concept
import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import okhttp3.{ConnectionPool, OkHttpClient}

class ClarifyClient(config: Config) extends StrictLogging {
  private val clientId = config.getString("clarifai.clientId")
  private val clientSecret = config.getString("clarifai.clientSecret")

  private val client = new ClarifaiBuilder(clientId, clientSecret)
    .client(new OkHttpClient().newBuilder()
      .readTimeout(5, TimeUnit.MINUTES)
      .connectionPool(new ConnectionPool(4, 60, TimeUnit.SECONDS))
      .build())
    .buildSync()

  def predictDir(path: String, callback: Callback[util.List[ClarifaiOutput[Concept]]], fileExt: String = "jpg"): Unit = {
    val images = new File(path).list().filter(_.endsWith(fileExt)).take(20)
    logger.debug(s"do prediction for ${images.length} images from $path, images: ${images.mkString(", ")}")
    images.grouped(5).seq.foreach(_.par.foreach(img => predict(new File(path, img), callback)))
  }

  def predict(img: File, callback: Callback[util.List[ClarifaiOutput[Concept]]]): Unit = {
    logger.debug(s"predict image: $img")
    // You can also do Clarifai.getModelByID("id") to get custom models
    client.getDefaultModels.generalModel()
      .predict()
      .withInputs(ClarifaiInput.forImage(ClarifaiImage.of(img)))
      .executeAsync(callback)
  }
}
