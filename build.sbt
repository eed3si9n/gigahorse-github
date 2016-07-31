lazy val baseVersion = "0.1.0-SNAPSHOT"
lazy val gigahorseVersion = "0.1.0"
lazy val gigahorseCore = "com.eed3si9n" %% "gigahorse-core" % gigahorseVersion
lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.0-RC4"
lazy val sbtIo = "org.scala-sbt" %% "io" % "1.0.0-M6"
// lazy val slip28JsonAst = "org.mdedetrich" %% "scala-json-ast" % "1.0.0-M1"
lazy val sjsonNewScalaJson = "com.eed3si9n" %%  "sjson-new-scalajson" % "0.4.1"

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
    libraryDependencies ++= List(gigahorseCore, sjsonNewScalaJson),
    // libraryDependencies += "net.databinder.dispatch" %% "dispatch-json4s-native" % dispatchVersion,
    libraryDependencies += scalatest % Test,
  //   initialCommands in console := """import dispatch._, Defaults._
  //                                   |import repatch.github.{request => gh}
  //                                   |val client = gh.LocalConfigClient()
  //                                   |val http = new Http""".stripMargin
    sourceManaged in (Compile, generateDatatypes) := (sourceDirectory in Compile).value / "scala",
    // You need this otherwise you get X is already defined as class.
    sources in Compile := (sources in Compile).value.toList.distinct
  )
