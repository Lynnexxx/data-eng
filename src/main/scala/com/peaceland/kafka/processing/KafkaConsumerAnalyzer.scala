


package com.peaceland.kafka.processing


import com.peaceland.utils.{Alert, Citizen, Drone, GetSource, Report}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.Json

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._

object KafkaConsumerAnalyzerApp extends App {

  // Configuration du consumer Kafka
  val props: Properties = new Properties()
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "test")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)

  consumer.subscribe(List("peaceLand").asJava)

  // Fonction pour calculer le score moyen des citoyens
  def calculateAverageScore(citizens: List[Citizen]): Double = {
    val scores = citizens.map(_.score)
    if (scores.nonEmpty)
      scores.sum.toDouble / scores.length.toDouble
    else
      0.0
  }

  // Fonction pour obtenir le score maximum et minimum des citoyens
  def getMinMaxScore(citizens: List[Citizen]): (Int, Int) = {
    val scores = citizens.map(_.score)
    (scores.max, scores.min)
  }

  // Fonction pour compter le nombre de citoyens par score
  def countCitizensByScore(citizens: List[Citizen]): Map[Int, Int] = {
    citizens.groupBy(_.score).mapValues(_.size)
  }

  // Fonction pour obtenir les citoyens les plus problématiques (score > 7)
  def getTroubleMakers(citizens: List[Citizen]): List[Citizen] = {
    citizens.filter(_.score > 7)
  }

  // Fonction pour analyser la répartition géographique
  def analyzeGeographicalDistribution(drones: List[Drone]): Map[(Double, Double), Int] = {
    drones.groupBy(drone => (drone.latitude, drone.longitude)).mapValues(_.size)
  }

  // Extraction des dates à partir des rapports
  val dates: List[String] = consumerRecords.asScala.toList.flatMap { record =>
  val recordValue = record.value()
  val parsedRecord = Json.parse(recordValue)
  val report = parsedRecord.as[Report]
  report.date
}
  // Fonction pour analyser les tendances temporelles
  def analyzeTemporalTrends(dates: List[String]): Map[String, Int] = {
    dates.groupBy(date => date.substring(0, 10)).mapValues(_.size)
  }

  // Fonction pour obtenir les mots les plus fréquents
  def getMostFrequentWords(reports: List[Report], limit: Int): List[String] = {
    val allWords = reports.flatMap(_.words.split(" "))
    allWords.groupBy(identity).mapValues(_.size).toList.sortBy(-_._2).take(limit).map(_._1)
  }

  // Menu pour sélectionner les statistiques à afficher
  def printMenu(): Unit = {
    println("==== Menu ====")
    println("1. Score moyen des citoyens")
    println("2. Score maximum et minimum des citoyens")
    println("3. Nombre de citoyens par score")
   
 }

   }