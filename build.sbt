name := "JackHenry"

version := "0.1"

scalaVersion := "2.13.6"

val Http4sVersion = "1.0.0-M21"
val CirceVersion = "0.14.0-M5"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s" %% "http4s-circe"        % Http4sVersion,
  "org.http4s" %% "http4s-dsl"          % Http4sVersion,
  "io.circe"   %% "circe-generic"       % CirceVersion,
  "io.circe"   %% "circe-core"          % CirceVersion,
  "io.circe"   %% "circe-parser"        % CirceVersion,
  "org.typelevel" %% "cats-core"        % "2.6.1"
)

libraryDependencies += "com.softwaremill.sttp.client3" %% "httpclient-backend-zio" % "3.3.11"

scalacOptions += "-Ymacro-annotations"