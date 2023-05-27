name := "test_kafka"

version := "1.0"

scalaVersion := "2.12.12"
val sparkVersion = "3.2.1"

libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "3.2.0"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "2.8.0"
libraryDependencies += "org.apache.kafka" %% "kafka-streams-scala" % "2.8.0"
libraryDependencies += "com.github.stevenchen3" % "scala-faker_2.12" % "0.1.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.2"
libraryDependencies += "javax.mail" % "mail" % "1.4.1"
libraryDependencies += "com.github.jurajburian" %% "mailer" % "1.2.3" //A wrapper based on javax.mail
//libraryDependencies += "it.bitbl" % "scala-faker_2.10" % "0.4"
//libraryDependencies += "com.github.javafaker" % "javafaker" % "1.0.2"




/*lazy val root = (project in file("."))
  .settings(
    name := "test_kafka"
  )*/
