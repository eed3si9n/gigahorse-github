import DispatchGithubKeys._

dispatchVersion := "0.8.5"

name := "dispatch-github"

organization := "com.eed3si9n"

scalaVersion := "2.9.1"

version <<= (dispatchVersion) { "dispatch" + _ + "_0.0.1-SNAPSHOT" }

libraryDependencies <++= (dispatchVersion) { (dv) => Seq(
  "net.databinder" %% "dispatch-core" % dv,
  "net.databinder" %% "dispatch-lift-json" % dv,
  "org.specs2" %% "specs2" % "1.5" % "test"
)}

crossScalaVersions := Seq("2.9.1", "2.8.1")
