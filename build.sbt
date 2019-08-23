import sbt.Keys._

scalaVersion := "2.12.4"
name         := "skeleton"
version      := "v1.0-SNAPSHOT"

PlayKeys.devSettings := Seq(
  "app.env" -> "test",
  "play.server.http.port" -> "9000"
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases")
)

libraryDependencies ++= Seq(
  jdbc,
  guice,
  ws,
  filters,
  "xingu"                  %% "xingu-scala-play"            % "v1.0-SNAPSHOT",
  "com.typesafe.slick"     %% "slick"                       % "3.2.1",
  "com.typesafe.slick"     %% "slick-hikaricp"              % "3.2.1",
  "org.postgresql"         %  "postgresql"                  % "42.1.4",
  "org.scalatestplus.play" %% "scalatestplus-play"          % "3.1.0" % Test,
  "org.scalamock"          %% "scalamock-scalatest-support" % "3.5.0" % Test
)


lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin)

topLevelDirectory    := None
executableScriptName := "run"
packageName in Universal := "package"