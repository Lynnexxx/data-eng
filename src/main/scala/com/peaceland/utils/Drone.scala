
package com.peaceland.utils
import com.peaceland.utils.Citizen
import io.alphash.faker.{Geolocation, Lorem}
case class Drone
(
//This case class represennt a drone of peaceland
  droneId : Int,
  latitude : Double,
  longitude : Double,
  citizens : List[Citizen],
  wordsHeared : String
//Rajouter les informations sur l'état du drone?
)

object Drone
{
  //This companion object generate a list of n drones
  val citizenNb = 10 //Number of citizens observed by a drone

  def generateDrone(n : Int): List[Drone]=
  {
    val drones = Range.inclusive(1,n).map(
      droneId =>
      {
        val location = Geolocation()
        val citizen = Citizen.generateCiziens(citizenNb)
        val paragraph = Lorem().paragraph
        Drone(
          droneId = droneId,
          latitude = location.latitute,
          longitude = location.longitude,
          citizens = citizen,
          wordsHeared = paragraph
        )
      }
    )
    drones.toList
  }
}