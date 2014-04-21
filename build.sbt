def baseVersion = "0.1.0-SNAPSHOT"
def dispatchVersion = "0.11.0"
def specsVersion = "2.3.11"

lazy val commonSettings: Seq[Def.Setting[_]] = Seq(
  version := s"dispatch${dispatchVersion}_${baseVersion}",
  organization := "com.eed3si9n",
  scalaVersion := "2.10.4",
  crossScalaVersions := Seq("2.10.4"),
  fork in run := true
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "repatch-github"
  ).aggregate(core)

lazy val core = (project in file("core")).
  settings(commonSettings: _*).
  settings(
    name := "repatch-github-core",
    libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % dispatchVersion,
    libraryDependencies += "net.databinder.dispatch" %% "dispatch-json4s-native" % dispatchVersion,
    libraryDependencies += "org.specs2" %% "specs2" % specsVersion % "test",
    initialCommands in console := """import dispatch._, Defaults._
                                    |import repatch.github.{request => gh}
                                    |val client = gh.LocalConfigClient()
                                    |val http = new Http""".stripMargin
  )
