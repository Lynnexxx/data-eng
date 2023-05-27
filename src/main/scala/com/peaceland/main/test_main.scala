package com.peaceland
import io.alphash.faker.{Geolocation, Lorem}
import com.peaceland.utils.{Alert, Drone}

object test_main
{
  def main(args: Array[String]): Unit =
  {
   // val alert: Alert = Alert("marie-lynne-murielle-essenahoun.agbaholou@efrei.net", "axel-loyanta.daoudongar@efrei.net", "Peaceland Alert", "The citizen " + "name" + " with id " + "id" + " has a score of " + "score" + ". TROUBLE MAKER!!!!!")
    val alert: Alert = Alert("marie-lynne-murielle-essenahoun.agbaholou@efrei.net", "axel-loyanta.daoudongar@efrei.net", "Peaceland Alert", "The citizen " + "name" + " with id " + "id" + " has a score of " + "score" + ". TROUBLE MAKER!!!!!")

    println(alert)
    Alert.generateAlert(alert)
  }

}
