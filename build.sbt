import DispatchGithubKeys._

dispatchVersion := "0.9.4"

name := "dispatch-github"

organization := "com.eed3si9n"

scalaVersion := "2.9.2"

version <<= (dispatchVersion) { "dispatch" + _ + "_0.0.1-SNAPSHOT" }

libraryDependencies <++= (dispatchVersion) { (dv) => Seq(
  "net.databinder.dispatch" %% "dispatch-core" % dv,
  "net.databinder.dispatch" %% "dispatch-lift-json" % dv,
  "org.specs2" %% "specs2" % "1.12.3" % "test"
)}

crossScalaVersions := Seq("2.9.2", "2.9.1", "2.9.0")
