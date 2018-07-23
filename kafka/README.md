# Kafka
kafka 관련 명령어 실행시 zookeeepr 정보가 필요 하기 때문에 환경 변수로 설정
```bash
echo "export ZOO_SERVER=\"zookeeper-01:2181,zookeeper-02:2181,zookeeper-03:2181/kwfarm\"" >> ~/.profile
echo "export KAFKA_SERVER=\"kafka-01:9092,kafka-02:9092,kafka-03:9092\"" >> ~/.profile
source ~/.profile
```

## zookeeper
역활
- 컨트롤러 선정
- 브로커 메타데이터
- 토픽 메타데이터
- 클라이언트 접속 정보
- 카프카 토픽 Acls

## partition
topic을 구성하는 단위이며 메세지를 저장(?) 하는 단위이다.
product가 메세지를 보내면 partition 단위로 저장된다.
partition내에는 offset이 존재하여, 순서가 보장 되지만 다수의 partition으로 구성 된 
topic내에서 메세지 순서는 보장되지 않는다.

## replication
replication이 구성시 topic 자체가 아닌 partition을 기준으로 복제 되며
partition은 leader와 flower로 구분되며, leader가 읽기, 쓰기를 담당한다.
flower는 leader의 정보를 가져다가 동기화 한다.

### ISR(in Sync Replica)
replication이 되고 있는 group이며 leader가 down시 isr에 속한 구성원만이 leader로 승격이 가능하다.
ISR로 replication을 관리하는 이유는 replication이 누락 된 flower는 leader로 승격이 되지 않아야 하기 때문이다.

```bash
# ISR 확인 방법
kafka-topics.sh --zookeeper $ZOO_SERVER --topic kwfarm --describe
Topic:kwfarm	PartitionCount:2	ReplicationFactor:2	Configs:
	Topic: kwfarm	Partition: 0	Leader: 1	Replicas: 1,2	Isr: 1,2
	Topic: kwfarm	Partition: 1	Leader: 3	Replicas: 2,3	Isr: 3,2

# 2번 Broker Down 후
kafka-topics.sh --zookeeper $ZOO_SERVER --topic kwfarm --describe
Topic:kwfarm	PartitionCount:2	ReplicationFactor:2	Configs:
	Topic: kwfarm	Partition: 0	Leader: 1	Replicas: 1,2	Isr: 1
	Topic: kwfarm	Partition: 1	Leader: 3	Replicas: 2,3	Isr: 3
```

## producer
### ask
메시지를 보낸 후 요청을 완료하기 전 처리 옵션
- 0: 메세지를 보내고 응답을 기다리지 않음
- 1: 리더의 데이터 기록 확인
- all: 리더 및 isr group의 동기화 응답을 기다림

all 사용시 kafka의 min.insync.replicas(최소 동기화 갯수) 옵션 값을 확인 해야합니다.
replication이 3이고, min.insync.replicas가 3인 경우 하나의 partition이 down시 
조건을 충족 할 수 없기 때문에 메세지를 정상 처리 할 수 없습니다.


## consumer
### consumer group

### commit





```bash
kafka-topics.sh --zookeeper $zoo_server \
--replication-factor 2 \
--partitions 2 \
--topic kwfarm2 \
--create


kafka-topics.sh --zookeeper $zoo_server --topic kwfarm2 --describe
Topic:kwfarm2	PartitionCount:2	ReplicationFactor:2	Configs:
	Topic: kwfarm2	Partition: 0	Leader: 3	Replicas: 3,2	Isr: 3,2
	Topic: kwfarm2	Partition: 1	Leader: 1	Replicas: 1,3	Isr: 1,3


kafka down



kafka-topics.sh --zookeeper $zoo_server --topic kwfarm2 --describe
Topic:kwfarm2	PartitionCount:2	ReplicationFactor:2	Configs:
	Topic: kwfarm2	Partition: 0	Leader: 3	Replicas: 3,2	Isr: 3
	Topic: kwfarm2	Partition: 1	Leader: 1	Replicas: 1,3	Isr: 1,3



{"version":1,
"partitions":[
        {"topic":"kwfarm2", "partition":0, "replicas": [3,1]},
        {"topic":"kwfarm2", "partition":1, "replicas": [1,3]}
]}


kafka-reassign-partitions.sh --zookeeper $zoo_server --reassignment-json-file rf.json --execute


kafka-topics.sh --zookeeper $zoo_server --topic kwfarm2 --describe
Topic:kwfarm2	PartitionCount:2	ReplicationFactor:2	Configs:
	Topic: kwfarm2	Partition: 0	Leader: 3	Replicas: 3,1	Isr: 3,1
	Topic: kwfarm2	Partition: 1	Leader: 1	Replicas: 1,3	Isr: 1,3


    

```

- replacation 되고 있는 group
- ISR에 속한 구성원망이 Leader로 승격 가능




console consumer
kafka-console-consumer.sh \
--bootstrap-server $KAFKA_SERVER \
--topic kwfarm \
--from-beginning


console producer
kafka-console-producer.sh \
--broker-list $KAFKA_SERVER \
--topic kwfarm
