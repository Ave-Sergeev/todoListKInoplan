import play.sbt.routes.RoutesKeys

name := """lessonApp"""

organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.0"
libraryDependencies += "org.typelevel" %% "cats-kernel" % "2.1.0"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.mockito" %% "mockito-scala" % "1.10.2"
libraryDependencies += "io.sentry" % "sentry-logback" % "1.7.23"
libraryDependencies += "io.sentry" % "sentry" % "1.7.23"
libraryDependencies += "net.kencochrane.raven" % "raven" % "6.0.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "biz.paluch.logging" % "logstash-gelf" % "1.14.0"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "1.0.0-play27",
  "org.reactivemongo" %% "reactivemongo" % "1.0.0"
)
