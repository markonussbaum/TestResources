ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

val sparkVersion = SettingKey[String]("sparkVersion")

ThisBuild / sparkVersion := "3.1.3"

lazy val root = (project in file ("."))
	.settings(
		name := "Hadoop Resource Test",
		libraryDependencies ++= Seq(
			"org.apache.spark" %% "spark-yarn" % sparkVersion.value,
			"org.scalatest" %% "scalatest" % "3.2.10" % "test"
		),
		version := "0.1"
	)

