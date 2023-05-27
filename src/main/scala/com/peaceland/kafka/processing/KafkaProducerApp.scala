
package com.peaceland.kafka.processing

import com.peaceland.utils._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.serialization.StringSerializer
import play.api.libs.json.Json

import java.util.{Properties, Timer, TimerTask}

object KafkaProducerApp //extends App
{
  //Création et configuration du producer
  val props: Properties = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put("acks","all")

  val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)// producer instance

  val nbDrones =10 //Number of drones in activity
  val topiC ="peaceLand"
  def droneData(): Unit = {
    println("Sending data to topic peaceLand")
    val drones = Drone.generateDrone(nbDrones)
    drones.foreach(drone => droneDataToKafka(drone))
  }

  def droneDataToKafka(drone: Drone): Unit = {
    implicit val reportImplicitWrites = Json.writes[Report]
    println(reportImplicitWrites)
    val droneReport = Json.toJson(Report.generateReport(drone)) //Converting repport in JSON format
    println(droneReport)
    val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topiC, drone.droneId.toString, droneReport.toString)
    producer.send(record, (recordMetadata: RecordMetadata, exception: Exception) => {
      if (exception != null) {
        exception.printStackTrace()
      }
      else {
        println(s"Metadata about the sent record : $recordMetadata")
      }
    })
    println("sending data via kafka" + droneReport.toString())

    producer.flush()
  }


  def main(args: Array[String]): Unit = {
    println("Starting producer ............")
    droneData()
    val timer = new Timer()
    val timerTask = new TimerTask { //Difining the timer task
      override def run(): Unit = { //override is used to redifine the existing function run()
        droneData()
      }
    }
    timer.schedule(timerTask, 50, 30000)
    /*
    * The first repport willl be sent after 50 milisecond
    * There is 5000 ms (5s) between the sending of each repport
    * */
  }
  //Envoi d'un message
  /*val key = "akey"
  val value = "my second message"
  val topic="text_topic"

  val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, key, value)
  producer.send(record, (recordMetadata: RecordMetadata, exception: Exception) => {
    if (exception != null) {
      exception.printStackTrace()
    }
    else {
      println(s"Metadata about the sent record : $recordMetadata")
    }
  })*/

}
