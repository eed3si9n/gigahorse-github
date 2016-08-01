lazy val baseVersion = "0.1.0-SNAPSHOT"
lazy val gigahorseVersion = "0.1.1"
lazy val gigahorseCore = "com.eed3si9n" %% "gigahorse-core" % gigahorseVersion
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.0-RC4"
lazy val sbtIo = "org.scala-sbt" %% "io" % "1.0.0-M6"
lazy val sjsonNewScalaJson = "com.eed3si9n" %%  "sjson-new-scalajson" % "0.4.2"
lazy val sjsonNewCore = "com.eed3si9n" %%  "sjson-new-core" % "0.4.2"

lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
  version := s"gigahorse${gigahorseVersion}_${baseVersion}",
  organization := "com.eed3si9n",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.11.8"),
  fork in run := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "gigahorse-github-root",
    publish := (),
    publishLocal := ()
  ).aggregate(core)

lazy val core = (project in file("core")).
  enablePlugins(DatatypePlugin, JsonCodecPlugin).
  settings(commonSettings: _*).
  settings(
    name := "gigahorse-github",
    libraryDependencies ++= List(gigahorseCore, sjsonNewScalaJson, sjsonNewCore),
    libraryDependencies += scalatest % Test,
    initialCommands in console := """import gigahorse._
                                    |import gigahorse.github.{ Github, response => res }
                                    |import scala.json.ast.unsafe._
                                    |import sjsonnew.support.scalajson.unsafe.CompactPrinter
                                    |import java.io.File
                                    |import scala.concurrent._
                                    |import scala.concurrent.duration._
                                    |val client = Github.localConfigClient""".stripMargin,
    datatypeCodecParents in (Compile, generateDatatypes) += "sjsonnew.BasicJsonProtocol",
    sourceManaged in (Compile, generateDatatypes) := (sourceDirectory in Compile).value / "scala",
    // You need this otherwise you get X is already defined as class.
    sources in Compile := (sources in Compile).value.toList.distinct,
    resolvers += Resolver.sonatypeRepo("public")
  )
