# Kafka
kafka 관련 명령어 실행시 zookeeepr 정보가 필요 하기 때문에 환경 변수로 설정
export ZOO_SERVER="zookeeper-01:2181,zookeeper-02:2181,zookeeper-03:2181/test"
export KAFKA_SERVER="kafka-01:9092,kafka-02:9092,kafka-03:9092"

## zookeeper
역활
- 컨트롤러 선정
- 브로커 메타데이터
- 토픽 메타데이터
- 클라이언트 ㅏㄹ당 정보
- 카프카 토픽 Acls


## Topic 만들기
kafka-topics.sh --zookeeper $zoo_server \
--replication-factor 2 \
--partitions 2 \
--topic kwfarm2 \
--create

# partition


# replication
topic 단위로 replication이 구성되는 것이 아니라 partition 별로 구성이 된다.
partition은 leader와 flower로 구분이 되며, leader가 읽기, 쓰기를 담당하며,
flower는 leader의 정보를 가져다 동기화를 진행한다.

ISR(in Sync Replica)
replication이 되고 있는 group이며 leader가 down시 isr에 속한 구성원만이 leader로 승격이 가능하다.



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