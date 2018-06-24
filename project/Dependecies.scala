import sbt._
import Keys._
import sbt.contraband.ContrabandPlugin.autoImport._

object Dependencies {

  lazy val gigahorseVersion = "0.3.1"
  val gigahorseCore = "com.eed3si9n" %% "gigahorse-core" % gigahorseVersion
  val gigahorseOkHttp = "com.eed3si9n" %% "gigahorse-okhttp" % gigahorseVersion
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.5"
  val sjsonNew = Def.setting { "com.eed3si9n" %% "sjson-new-core" % contrabandSjsonNewVersion.value }
  val sjsonNewScalaJson = Def.setting { "com.eed3si9n" %% "sjson-new-scalajson" % contrabandSjsonNewVersion.value }
  val sjsonNewSpray = Def.setting { "com.eed3si9n" %% "sjson-new-spray" % contrabandSjsonNewVersion.value }

  lazy val jawnVersion = "0.12.1"
  lazy val jawnSpray = "org.spire-math" %% "jawn-spray" % jawnVersion
}
