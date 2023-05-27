
package com.peaceland.kafka.processing

import com.peaceland.utils.Alert
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.Json

import java.time.Duration
import java.util.Properties
import scala.jdk.CollectionConverters._

object KafkaConsumerSubscribeApp extends App
{
  //Creation et configuration du consumer
  val props: Properties = new Properties()
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "test")
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer])
  props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true")
  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000")

  val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props) //consumer instance

  consumer.subscribe(List("peaceLand").asJava)
  //Consuming the repport
  val consumerRecords: ConsumerRecords[String, String] = consumer.poll(Duration.ofSeconds(60))
  consumerRecords.asScala.foreach { record =>
    println(s"offset = ${record.offset()}, key = ${record.key()}, value = ${record.value()}")
    val recordValue = record.value()
    println("==============PARSED TEST&RESULTS================")
    val parsedRecord = Json.parse(recordValue)
    println(parsedRecord)
    val droneId = (parsedRecord \\ "droneId").hwead.as[Int]
    println(droneId)
    val words = (parsedRecord \\ "words").head.as[String]
    println(words)
    val citizensName = (parsedRecord \\ "citizensName").toList.head.as[List[String]]
    println(citizensName)
    val citizensId = (parsedRecord \\ "citizensId").toList.head.as[List[Int]]
    val citizensScore = (parsedRecord \\ "citizensScore").toList.head.as[List[Int]]

    (citizensId, citizensScore, citizensName).zipped.foreach((id, score, name) => {
      if (score > 7) {
        //val alert : Alert = Alert("daoudongaraxel@gmail.com", "axel-loyanta.daoudongar@efrei.net", "Peaceland Alert", "The citizen " +name+" with id "+id+ " has a score of " +score+". TROUBLE MAKER!!!!!")
        val alert: Alert = Alert("marie-lynne-murielle-essenahoun.agbaholou@efrei.net", "axel-loyanta.daoudongar@efrei.net", "Peaceland Alert", "A trouble maker finded !\n"+ "The citizen : " +name+ "\nId : " + id + "\nHas a score of : " + score)
        println("A trouble maker finded !\n"+ "The citizen : " +name+ "\nId : " + id + "\nHas a score of : " + score)
        Alert.generateAlert(alert)
      }
    })

  }

  consumer.close()
}
