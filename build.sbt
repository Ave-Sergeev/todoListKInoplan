import play.sbt.routes.RoutesKeys

name := """lessonApp"""

organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "1.0.0-play27",
  "org.reactivemongo" %% "reactivemongo" % "1.0.0"
)