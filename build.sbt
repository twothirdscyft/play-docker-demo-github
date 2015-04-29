import NativePackagerKeys._

name := "play-docker-demo"

version := "1.0-SNAPSHOT"

lazy val WrapIntTest = config("it") extend (Test)
lazy val testAll = TaskKey[Unit]("test-all")

lazy val wrapIntTestSettings = inConfig(WrapIntTest)(Defaults.testSettings) ++
  Seq(
    sourceDirectory in WrapIntTest := baseDirectory.value / "it",
    scalaSource in WrapIntTest := baseDirectory.value / "it"
  ) ++
  Seq(
    testAll <<= (test in WrapIntTest).dependsOn(test in Test)
  )
    
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(WrapIntTest)
  .settings(wrapIntTestSettings: _*)

scalaVersion := "2.11.1"

resolvers += (
  "google-sedis-fix" at "http://pk11-scratch.googlecode.com/svn/trunk"
)

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1"
)

// Packaging
//// Docker
maintainer := "Thien Nguyen"

dockerBaseImage := "java:openjdk-7-jre"

dockerExposedPorts in Docker := Seq(9000)
