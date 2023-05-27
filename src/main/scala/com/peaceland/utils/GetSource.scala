package com.peaceland.utils
import scala.io.Source

object GetSource
{
  // Read from a CSV file embedded in the JAR file with getResource
  // There is exactly one entry on each line, so we use getLines
  val names: List[String] =Source.fromFile("src/main/resources/names.csv").getLines().toList
}
