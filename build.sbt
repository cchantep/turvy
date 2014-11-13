name := "turvy-play-demo"

version := "1.0-play23"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.3"

resolvers += "Applicius Releases" at "https://raw.github.com/applicius/mvn-repo/master/releases/"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.1",
  "fr.applicius.turvy" %% "vat-client" % "1.0")
