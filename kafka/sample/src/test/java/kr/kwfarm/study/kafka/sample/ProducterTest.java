package kr.kwfarm.study.kafka.sample;

import kafka.server.KafkaConfig;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
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

    @BeforeEach
    void beforeEach() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "kafka-01:9092,kafka-02:9092,kafka-03:9092");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());

        producer = new KafkaProducer<>(properties);
    }

    @AfterEach
    void afterEach() {
        producer.close();
    }

    @Test
    void 보내고안녕() {
        producer.send(new ProducerRecord<>("kwfarm", "Hello World1111"));
    }

    @Test
    void 동기처리() throws ExecutionException, InterruptedException {
        Future<RecordMetadata> recordMetadata = producer.send(new ProducerRecord<>("kwfarm", "fffff"));
        logger.info("{}", recordMetadata.get());
    }

    @Test
    void 비동기처리() {
        producer.send(new ProducerRecord<>("kwfarm", "aaaaa"), new Callback() {
            @Override
            public void onCompletion(RecordMetadata metadata, Exception exception) {
                logger.info("{}", metadata);
                logger.info("{}", exception == null);
            }
        });
    }
}
