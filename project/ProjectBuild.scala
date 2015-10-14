import sbt._
import sbt.Keys._

object ProjectBuild extends Build {
  import Settings._

  lazy val root = Project(
    id = "root",
    base = file("."),
    settings = parentSettings,
    aggregate = Seq(bukaCore, bukaMongo, bukaRest, bukaApp)
  )

  lazy val bukaCore = Project(
    id = "buka-core",
    base = file("./buka-core"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.bukaCore)
  )

  lazy val bukaMongo = Project(
    id = "buka-mongo",
    base = file("./buka-mongo"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.bukaMongo)
  ).dependsOn(bukaCore)

  lazy val bukaRest = Project(
    id = "buka-rest",
    base = file("./buka-rest"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.bukaRest)
  ).dependsOn(bukaMongo)

  lazy val bukaApp = Project(
    id = "buka-app",
    base = file("./buka-app"),
    settings = defaultSettings ++ Seq(libraryDependencies ++= Dependencies.bukaApp)
  ).dependsOn(bukaRest)
}

object Dependencies {
  import Versions._

  object Compile {
    val config        = "com.typesafe"             % "config"               % TypesafeConfigVer
    val ssh           = "com.decodified"          %% "scala-ssh"            % ScalaSshVer
    val logback       = "ch.qos.logback"           % "logback-classic"      % LogbackVer
    val bouncy        = "org.bouncycastle"         % "bcprov-jdk16"         % "1.46"
    val jcraft        = "com.jcraft"               % "jzlib"                % "1.1.3"
  }

  object Test {
    val scalatest     = "org.scalatest"           %% "scalatest"            % ScalaTestVer      % "test"
    val scalacheck    = "org.scalacheck"          %% "scalacheck"           % ScalaCheckVer     % "test"
    val junit         = "junit"                    % "junit"                % JunitVer          % "test"
  }

  import Compile._

  val test = Seq(Test.scalatest, Test.scalacheck, Test.junit)

  /** Module deps */

  val bukaCore = Seq(config, ssh, logback, bouncy, jcraft) ++ test
  val bukaMongo = Seq(config) ++ test
  val bukaRest = Seq(config) ++ test
  val bukaApp = Seq(config) ++ test
}
