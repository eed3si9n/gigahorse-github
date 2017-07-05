import sbt._
import Keys._
import sbt.contraband.ContrabandPlugin.autoImport._

object Dependencies {

  lazy val gigahorseVersion = "0.3.1"
  val gigahorseCore = "com.eed3si9n" %% "gigahorse-core" % gigahorseVersion
  val gigahorseOkHttp = "com.eed3si9n" %% "gigahorse-okhttp" % gigahorseVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"
  lazy val sbtIo = "org.scala-sbt" %% "io" % "1.0.0-M6"
  val sjsonNew = Def.setting { "com.eed3si9n" %% "sjson-new-core" % contrabandSjsonNewVersion.value }
  val sjsonNewScalaJson = Def.setting { "com.eed3si9n" %% "sjson-new-scalajson" % contrabandSjsonNewVersion.value }
}
