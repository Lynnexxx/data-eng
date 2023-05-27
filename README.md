# data_engineering_project

Procédure Kafka et Kafka Connect :

#Changez le path, le mien /home/paulc/Téléchargements/
#Kafka connect et Azure Cosmos DB
#Install and configure zookeeper, Kafka, Kafka Connect et Azure Cosmos DB connectors

#télécharger le latest kafka-connect-cosmos-<version>-jar-with-dependencies.jar file de kafka-connect-cosmodb sur https://github.com/microsoft/kafka-connect-cosmosdb/releases et le copier dans le répertoire/plugin kafka-connect créé. EDIT le copier dans le dossier libs en fait

#start kafka server
/home/paulc/Téléchargements/kafka_2.12-2.5.0/bin/kafka-server-start.sh /home/paulc/Téléchargements/kafka_2.12-2.5.0/config/server.properties

#start zookeeper server
/home/paulc/Téléchargements/kafka_2.12-2.5.0/bin/zookeeper-server-start.sh /home/paulc/Téléchargements/kafka_2.12-2.5.0/config/zookeeper.properties

#Aller sur Azure et lancer le cloud shell. Suivre les instructions du fichier setAZCosmosDB

#start kafka-connect
/home/paulc/Téléchargements/kafka_2.12-2.5.0/bin/connect-distributed.sh /home/paulc/Téléchargements/kafka_2.12-2.5.0/config/connect-distributed.properties

Avant de lancer le curl, vérifier que le fichier cosmos-sink-connector.json est bien configuré notamment
le connection.endpoint et le master key. Voir les commandes pour récupérer leurs valeurs dans le setAZCosmosDB. N'oubliez pas de changer le topic et le containers.topicmap. Dans hotels#kafka, kafka c'est le nom du container.

Lancer la commande curl:
curl -H "Content-Type: application/json" -X POST -d @/home/paulc/Documents/Efrei/dataEngineering/data_engineering_project/src/main/scala/com/peaceland/cosmos-sink-connector.json http://localhost:8083/connectors https://peacewatcher-db2.documents.azure.com:443/

Si besoin pour supprimer le connector après avoir fait des changements dans le sink-connector.json
curl -X DELETE http://localhost:8083/connectors/cosmosdb-sink-connector


##TESTS simple producer pour vérifier qu'on récupère bien des simples données
/home/paulc/Téléchargements/kafka_2.12-2.5.0/bin/kafka-console-producer --broker-list localhost:9092 --topic hotels
entrer les données json dans le producer ex :
{"id": "h1", "HotelName": "Marriott", "Description": "Marriott description"}
{"id": "h2", "HotelName": "HolidayInn", "Description": "HolidayInn description"}
{"id": "h3", "HotelName": "Motel8", "Description": "Motel8 description"}


#Une fois que les tests sont OK et que les données apparaissent bien dans la db créée sur Azure
Modifier le cosmos-sink-connector.json avec les bons topics, relancer le connector et lancer les producers et consumer kafka. Il se peut que le format .json ne soit pas bon et qu'il doive respecter un format
Voir https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/kafka-connector-sink#read-a-json-message-without-the-expected-schemapayload-structure




#Pas nécessaire
#Créer le plugin kafka-connect s'il n'existe pas déjà dans /usr/share/java
sudo mkdir -p /usr/share/java/kafka-connect/
sudo chown -R <your-username>:<your-group> /usr/share/java/kafka-connect/
sudo chmod -R 755 /usr/share/java/kafka-connect/


useful links :
https://learn.microsoft.com/en-us/azure/cosmos-db/nosql/kafka-connector-sink
https://github.com/microsoft/kafka-connect-cosmosdb/blob/dev/doc/CosmosDB_Setup.md
https://docs.confluent.io/platform/current/connect/confluent-hub/client.html#confluent-hub-client
https://github.com/microsoft/kafka-connect-cosmosdb/releases
https://docs.confluent.io/platform/current/connect/install.html#install-connector-manually
