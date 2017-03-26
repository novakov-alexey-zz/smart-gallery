name := "SmartGallery"

version := "1.0"

scalaVersion := "2.12.1"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

val scalaLoggingVersion = "3.5.0"
val logbackVersion = "1.1.2"

libraryDependencies ++= Seq(
  //server
  "com.sksamuel.elastic4s" %% "elastic4s-core" % "5.2.11",
  "com.sksamuel.elastic4s" %% "elastic4s-http" % "5.2.11",
  "com.sksamuel.elastic4s" %% "elastic4s-jackson" % "5.2.11",
  "com.clarifai.clarifai-api2" % "core" % "2.0.2",
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  //web
  "org.webjars" % "jquery" % "3.2.0",

  //test
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)