
package com.peaceland.utils

case class Report(droneId: Int,
                  latitude: Double,
                  longitude: Double,
                  citizensId: List[Int],
                  citizensScore: List[Int],
                  citizensName: List[String],
                  words: String
                 )


object Report {

  // This companion object generate a report using drone data
  def generateReport(drone: Drone): Report ={

    Report(
      droneId = drone.droneId,
      latitude = drone.latitude,
      longitude = drone.longitude,
      citizensId = drone.citizens.map(citizen => citizen.citizenId).toList,
      citizensScore = drone.citizens.map(citizen => citizen.score).toList,
      citizensName = drone.citizens.map(citizen => citizen.name).toList,
      words = drone.wordsHeared
    )
  }

}
