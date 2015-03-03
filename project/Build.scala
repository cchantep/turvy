import sbt._
 
object Builds extends Build {
  import Keys._
  import sbtscalaxb.Plugin._
  import ScalaxbKeys._

  lazy val getWsdl = taskKey[Unit]("Get WSDL")

  lazy val root = Project(id = "turvy", base = file("."),
    settings = scalaxbSettings).settings(
    scalaVersion := "2.11.3",
      organization := "turvy",
      name := "vat-client",
      version := "1.0",
      getWsdl := {
        val wsdl = scala.io.Source.fromURL(
          "http://ec.europa.eu/taxation_customs/vies/checkVatService.wsdl",
          "UTF-8")

        val wdir = (sourceDirectory in Compile).value / "wsdl"
        wdir.mkdirs()

        val f = wdir / "checkVatService.wsdl"

        IO.writer(f, "", IO.defaultCharset, false) { w =>
          w.append(wsdl.mkString)
        }
      },
      packageName in scalaxb in Compile := "eu.europa.ec",
      libraryDependencies ++= Seq(
        "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.2" % "provided",
        "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.2" % "provided",
        "net.databinder.dispatch" %% "dispatch-core" % "0.11.1" % "provided"),
      sourceGenerators in Compile <+= (scalaxb in Compile).dependsOn(getWsdl))
}
