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
topic은 하나 이상의 partition으로 구성되며 메세지를 저장 하는 단위이다.
partition내에는 offset이 존재하여, partition안의 메세지는 순서가 보장 되지만 다수의 partition으로 구성 된 
topic내에서 메세지 순서는 보장되지 않는다.

## replication
replication이 구성시 topic이 아닌 partition을 기준으로 복제 되며
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
하나의 partition은 하나의 consumer만 메세지를 가져 갈 수 있으며(consumer는 다수의 partition에서 메세지를 가져 올 수 있다.) 각 consumer의 partition 소유권을 분배 하는 기준이 consumer group이다.
consumer group내에서 consumer의 추가로 인하여 partition 소유권이 이동하는 것을 리밸런스(rebalance)라 한다.

하나의 topic에 대해서 다수개의 consumer group이 메세지를 가져갈 수 있으며, 각 group별로 offeset이 관리 된다.

### commit
consumer에서 메세지를 pull 한 후 위치 정보를 업데이트 하는 것
- 자동 커밋
- 수동 커밋
