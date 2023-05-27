package com.peaceland.kafka.processing

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.streaming._
import com.peaceland.utils._

object KafkaProducerApp {
  def main(args: Array[String]): Unit = {
    // Create the Spark session
    val spark = SparkSession
      .builder()
      .appName("Peacland_storage")
      .master("local[*]")
      .config("some.config.option", "some-value")
      .getOrCreate()

    // Use the case class Report to define the schema of the Spark DataFrame
    val mySchema = Encoders.product[Report].schema

    // Define the Kafka topic and bootstrap server
    val topic = "peaceLand"
    val bootstrapServer = "localhost:9092"

    // Read data from the Kafka stream
    val kafkaToDf: DataFrame = spark
      .readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServer)
      .option("subscribe", topic)
      .option("startingOffsets", "earliest")
      .load()

    // Transform the retrieved values
    val df = kafkaToDf
      .select(from_json(col("value").cast("string"), mySchema).as("Data_report"))
      .select("Data_report.*")
      .withColumnRenamed("droneId", "id")
      .withColumn("id", col("id").cast(StringType))

    // Configure Azure Cosmos DB
    val cosmosEndpoint = "https://data-ing.documents.azure.com:443/"
    val cosmosMasterKey = "1s9ljz9iHzSINDtnGYiCrJOHy9oEzJiSAF9LqPbD7yyEGN1FolSyFNeM4SMgXtkOVZXHWHKQdWLKACDbc9j5LQ==" // Replace with your key
    val cosmosDatabaseName = "sampleDB"
    val cosmosContainerName = "sampleContainer"

    val cfg = Map(
      "spark.cosmos.accountEndpoint" -> cosmosEndpoint,
      "spark.cosmos.accountKey" -> cosmosMasterKey,
      "spark.cosmos.database" -> cosmosDatabaseName,
      "spark.cosmos.container" -> cosmosContainerName
    )

    // Configure the Cosmos Catalog API to be used
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog", "com.azure.cosmos.spark.CosmosCatalog")
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog.spark.cosmos.accountEndpoint", cosmosEndpoint)
    spark.conf.set(s"spark.sql.catalog.cosmosCatalog.spark.cosmos.accountKey", cosmosMasterKey)

    // Write the initial DataFrame to Cosmos DB
    spark.createDataFrame(Seq(("cat-alive1", "Schrodinger cat", 2, true), ("cat-dead1", "Schrodinger cat", 2, false)))
      .toDF("id", "name", "age", "isAlive")
      .write
      .format("cosmos.oltp")
      .options(cfg)
      .mode("APPEND")
      .save()

    // Write the streaming data to Cosmos DB
    val query = df
      .writeStream
      .format("cosmos.oltp")
      .options(cfg)
      .option("checkpointLocation", "./checkpoints/reports")
      .outputMode(OutputMode.Append())
      .start()

    query.awaitTermination()

