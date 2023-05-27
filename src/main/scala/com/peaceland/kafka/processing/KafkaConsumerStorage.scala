package com.peaceland.kafka.processing
import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.streaming._
import com.peaceland.utils._

object KafkaConsumerStorage
{
  def main(args: Array[String]): Unit =
  {
    //Creation du spark session
    val spark = SparkSession
      .builder()
      .appName("Peacland_storage")
      .master("local[*]")
      .config("some.config.option", "some-value")
      .getOrCreate()

    //Utilisation de la case class Report pour définir le schéma de notre spark data frame
    val mySchema = Encoders. product[Report].schema

    //Récuparation des données de la stream
    val topiC = "peaceLand"
    val bootstrapServer = "localhost:9092"

    val kafkaToDf : DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServer)
      .option("subscribe", topiC)
      .option("startingOffsetes", "earliest")
      .load()

    //TRansformation des valeurs récupérées
    val df = kafkaToDf
      .select(from_json(col("value").cast("string"),mySchema).as("Data_report"))
      .select("Data_report.*")
      .withColumnRenamed("droneId","id")
      .withColumn("id",col("id").cast(StringType))


    //AZURE
    val cosmosEndpoint = "https://data-ing.documents.azure.com:443/"
    val cosmosMasterKey = "1s9ljz9iHzSINDtnGYiCrJOHy9oEzJiSAF9LqPbD7yyEGN1FolSyFNeM4SMgXtkOVZXHWHKQdWLKACDbc9j5LQ==" //Définir une secrets github?
    val cosmosDatabaseName = "sampleDB"
    val cosmosContainerName = "sampleContainer"

    val cfg = Map("spark.cosmos.accountEndpoint" -> cosmosEndpoint,
      "spark.cosmos.accountKey" -> cosmosMasterKey,
      "spark.cosmos.database" -> cosmosDatabaseName,
      "spark.cosmos.container" -> cosmosContainerName
    )

    // Configure Catalog Api to be used
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog", "com.azure.cosmos.spark.CosmosCatalog")
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog.spark.cosmos.accountEndpoint", cosmosEndpoint)
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog.spark.cosmos.accountKey", cosmosMasterKey)

    spark.createDataFrame(Seq(("cat-alive1", "Schrodinger cat", 2, true), ("cat-dead1", "Schrodinger cat", 2, false)))
      .toDF("id", "name", "age", "isAlive")
      .write
      .format("cosmos.oltp")
      .options(cfg)
      .mode("APPEND")
      .save()

    //Ecriture des données dans la stream
    df
      .writeStream
      .format("cosmos.oltp")
      .options(cfg)
      .option("checkpointLocation", "./checkpoints/reports")
      .outputMode(OutputMode.Append())
      .start()
      .awaitTermination()

    //l'éceiture dans la database se fait de manière continue
    //solution? rajouter une contrainte temporelle avec java timer?
    spark.close()
  }
}
