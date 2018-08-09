package kr.kwfarm.study.kafka.sample;

import kr.kwfarm.study.kafka.sample.builder.ProducerBuilder;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducterTest {
    private Logger logger = LoggerFactory.getLogger(ProducterTest.class);

    private KafkaProducer<String, String> producer;

    private Properties properties;

    @BeforeEach
    void beforeEach() {
        properties = ProducerBuilder.getBuilder().build();
    }

    @AfterEach
    void afterEach() {
        producer.close();
    }

    @Test
    void 보내고안녕() {
        producer = new KafkaProducer<String, String>(properties);

        producer.send(new ProducerRecord<>("kwfarm", "Hello World1111"));
    }

    @Test
    void 동기처리() throws ExecutionException, InterruptedException {
        producer = new KafkaProducer<String, String>(properties);
        Future<RecordMetadata> recordMetadata = producer.send(new ProducerRecord<>("kwfarm", "Helo World2"));
        logger.info("{}", recordMetadata.get());
    }

    @Test
    void 비동기처리() {
        producer = new KafkaProducer<String, String>(properties);
        producer.send(new ProducerRecord<>("kwfarm", "비동기 처리"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                logger.info("{}", metadata);
                logger.info("{}", exception == null);
            }
        });
    }

    @Test
    void partition지정() {
        producer = new KafkaProducer<String, String>(properties);
        // kafka-console-consumer.sh를 활용하여 각 partition별로 메세지가 오는 것을 확인 가능
        // kafka-console-consumer.sh --bootstrap-server kafka-01:9092,kafka-02:9092,kafka-03:9092 --topic kwfarm --partition 0
        // kafka-console-consumer.sh --bootstrap-server kafka-01:9092,kafka-02:9092,kafka-03:9092 --topic kwfarm --partition 1
        for (int i = 0; i < 10; i++) {
            int partitionKey = i % 2;
            String message = String.format("Partition 지정: %d, value: %d", partitionKey, i);
            logger.info("{}", partitionKey);
            producer.send(new ProducerRecord<>("kwfarm", partitionKey, null, message));
        }
    }
}
