![kafka-for-developers-using-spring-boot](https://github.com/dilipsundarraj1/kafka-for-developers-using-spring-boot/workflows/kafka-for-developers-using-spring-boot/badge.svg)
# kafka-for-developers-using-spring-boot

This repository has the complete code related to kafka producers/consumers using spring boot.

## Kafka SetUp
- [Setup-Kafka-Using-Docker](SetUpKafkaDocker.md)

## Securing your Kafka Cluster using SSL

- [Kafka SSL SetUp](https://github.com/dilipsundarraj1/kafka-cluster-ssl)

## H2 Database

- Access the h2 database in the following link - http://localhost:8081/h2-console


# Set Up Kafka in Local using Docker

## Set up broker and zookeeper

- Navigate to the path where the **docker-compose.yml** is located and then run the below command.

```
docker-compose up
```

## Producer and Consume the Messages

- Let's going to the container by running the below command.

```
docker exec -it kafka1 bash
```

- Create a Kafka topic using the **kafka-topics** command.
    - **kafka1:19092** refers to the **KAFKA_ADVERTISED_LISTENERS** in the docker-compose.yml file.

```
kafka-topics --bootstrap-server kafka1:19092 \
             --create \
             --topic test-topic \
             --replication-factor 1 --partitions 1
```

- Produce Messages to the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-producer --bootstrap-server kafka1:19092 \
                       --topic test-topic
```

- Consume Messages from the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server kafka1:19092 \
                       --topic test-topic \
                       --from-beginning
```

## Producer and Consume the Messages With Key and Value

- Produce Messages with Key and Value to the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-producer --bootstrap-server kafka1:19092 \
                       --topic test-topic \
                       --property "key.separator=-" --property "parse.key=true"
```

- Consuming messages with Key and Value from a topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server kafka1:19092 \
                       --topic test-topic \
                       --from-beginning \
                       --property "key.separator= - " --property "print.key=true"
```

### Consume Messages using Consumer Groups


```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server kafka1:19092 \
                       --topic test-topic --group console-consumer-41911\
                       --property "key.separator= - " --property "print.key=true"
```

- Example Messages:

```
a-abc
b-bus
```

### Consume Messages With Headers

```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server kafka1:19092 \
                       --topic library-events.DLT \
                       --property "print.headers=true" --property "print.timestamp=true" 
```

- Example Messages:

```
a-abc
b-bus
```

### Set up a Kafka Cluster with 3 brokers

- Run this command and this will spin up a kafka cluster with 3 brokers.

```
docker-compose -f docker-compose-multi-broker.yml up
```

- Create topic with the replication factor as 3

```
docker exec --interactive --tty kafka1  \
kafka-topics --bootstrap-server kafka1:19092 \
             --create \
             --topic test-topic \
             --replication-factor 3 --partitions 3
```

- Produce Messages to the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-producer --bootstrap-server localhost:9092,kafka2:19093,kafka3:19094 \
                       --topic test-topic
```

- Consume Messages from the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server localhost:9092,kafka2:19093,kafka3:19094 \
                       --topic test-topic \
                       --from-beginning
```
#### Log files in Multi Kafka Cluster

- Log files will be created for each partition in each of the broker instance of the Kafka cluster.
-  Login to the container **kafka1**.
  ```
  docker exec -it kafka1 bash
  ```
-  Login to the container **kafka2**.
  ```
  docker exec -it kafka2 bash
  ```

- Shutdown the kafka cluster

```
docker-compose -f docker-compose-multi-broker.yml down
```

### Setting up min.insync.replica

- Topic - test-topic

```
docker exec --interactive --tty kafka1  \
kafka-configs  --bootstrap-server localhost:9092 --entity-type topics --entity-name test-topic \
--alter --add-config min.insync.replicas=2
```

- Topic - library-events

```
docker exec --interactive --tty kafka1  \
kafka-configs  --bootstrap-server localhost:9092 --entity-type topics --entity-name library-events \
--alter --add-config min.insync.replicas=2
```
## Advanced Kafka Commands

### List the topics in a cluster

```
docker exec --interactive --tty kafka1  \
kafka-topics --bootstrap-server kafka1:19092 --list
```

### Describe topic

- Command to describe all the Kafka topics.

```
docker exec --interactive --tty kafka1  \
kafka-topics --bootstrap-server kafka1:19092 --describe
```

- Command to describe a specific Kafka topic.

```
docker exec --interactive --tty kafka1  \
kafka-topics --bootstrap-server kafka1:19092 --describe \
--topic test-topic
```

### Alter topic Partitions

```
docker exec --interactive --tty kafka1  \
kafka-topics --bootstrap-server kafka1:19092 \
--alter --topic test-topic --partitions 40
```

### How to view consumer groups

```
docker exec --interactive --tty kafka1  \
kafka-consumer-groups --bootstrap-server kafka1:19092 --list
```

#### Consumer Groups and their Offset

```
docker exec --interactive --tty kafka1  \
kafka-consumer-groups --bootstrap-server kafka1:19092 \
--describe --group console-consumer-41911
```

## Log file and related config

- Log into the container.

```
docker exec -it kafka1 bash
```

- The config file is present in the below path.

```
/etc/kafka/server.properties
```

- The log file is present in the below path.

```
/var/lib/kafka/data/
```

### How to view the commit log?

```
docker exec --interactive --tty kafka1  \
kafka-run-class kafka.tools.DumpLogSegments \
--deep-iteration \
--files /var/lib/kafka/data/test-topic-0/00000000000000000000.log
```

# kafka-cluster-ssl

This repo has the repo for the kafka-cluster-ssl.

## Generate the required certs for setting up the ssl secured cluster

- Navigate to the **secrets** directory.

- Run the below command.

```script
sh create-certs.sh
```

## Produce/Consumer Messages in a SSL Secured Environment

- Produce Messages to the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-producer --bootstrap-server localhost:9092 \
                       --topic test-topic \
                       --producer.config /etc/kafka/properties/producer.properties
```

- Produce Messages to the topic.

```
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server localhost:9092 \
                       --topic test-topic \
                       --from-beginning \
                       --consumer.config /etc/kafka/properties/consumer.properties
```


```agsl
docker exec --interactive --tty kafka1  \
kafka-console-consumer --bootstrap-server localhost:9092 \
                       --topic library-events \
                       --from-beginning \
                       --consumer.config /etc/kafka/properties/consumer.properties
```