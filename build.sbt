name := "dispatch-github"

organization := "com.eed3si9n"

version := "0.0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "net.databinder" %% "dispatch-core" % "0.8.5",
  "net.databinder" %% "dispatch-lift-json" % "0.8.5",
  "org.specs2" %% "specs2" % "1.5" % "test"
)

crossScalaVersions := Seq("2.9.1", "2.8.1")
