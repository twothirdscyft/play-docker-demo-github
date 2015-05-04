//import NativePackagerKeys._
import com.typesafe.sbt.packager.docker._

name := "play-docker-demo"

version := "0.2-SNAPSHOT"

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
  .enablePlugins(DockerPlugin)
  .configs(WrapIntTest)
  .settings(wrapIntTestSettings: _*)

scalaVersion := "2.11.1"

resolvers += (
  "google-sedis-fix" at "http://pk11-scratch.googlecode.com/svn/trunk"
)

libraryDependencies ++= Seq(
  cache,
  ws,
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1",
  "org.webjars" % "angularjs" % "1.3.2"
)

// Packaging
//// Docker
maintainer := "Thien Nguyen"

dockerBaseImage := "java:openjdk-7-jre"

dockerCommands ++= Seq(
  Cmd("ENV", "APP_REDIS_HOST=redis"),
  Cmd("ENV", "APP_REDIS_PORT=6379")
)

dockerRepository := Some("twothirdscyft")

dockerExposedPorts in Docker := Seq(9000)
