import NativePackagerKeys._

name := "play-docker-demo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)

// Packaging
//// Docker
maintainer := "Thien Nguyen"

dockerBaseImage := "java:openjdk-7-jre"

dockerExposedPorts in Docker := Seq(9000)
