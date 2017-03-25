name := "SmartGallery"

version := "1.0"

scalaVersion := "2.12.1"

val scalaLoggingVersion = "3.5.0"
val logbackVersion = "1.1.2"
val loggingScala = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
val loggingLogback = "ch.qos.logback" % "logback-classic" % logbackVersion

libraryDependencies ++= Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.2.11",
  "com.sksamuel.elastic4s" %% "elastic4s-http" % "5.2.11",
  "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "5.2.11",
  "com.clarifai.clarifai-api2" % "core" % "2.0.2",
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "com.typesafe.akka" %% "akka-http-core" % "10.0.5",
  loggingScala,
  loggingLogback,

  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
    