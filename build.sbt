name := "form_sample"

version := "1.0"

lazy val `form_sample` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws , "org.postgresql" % "postgresql" % "9.2-1004-jdbc41")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  