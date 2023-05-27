package com.peaceland.utils
package com.peaceland.utils
import scala.util.Random

case class Citizen
(
  //This case class represennt a citizen of peaceland
  citizenId : Int,
  name : String,
  score : Int
)


object Citizen
{
  //This companion object generate a list of n citizens
  val scoreMax=10
  val scoreMin=0

  def generateCiziens(n : Int): List[Citizen]=
  {
    val citizens = Range.inclusive(1, n).map(
      citizenId=>{
      val name = GetSource.names(Random.nextInt(GetSource.names.length)) //Citizen name
      val score = Random.nextInt((scoreMax - scoreMin)+1) //Citzen socre
      Citizen(citizenId, name, score)
    })
    citizens.toList
  }
}

