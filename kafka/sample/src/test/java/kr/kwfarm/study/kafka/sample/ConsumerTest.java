package kr.kwfarm.study.kafka.sample;

import kr.kwfarm.study.kafka.sample.builder.ConsumerBuilder;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

public class ConsumerTest {
    private Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    private KafkaConsumer<String, String> consumer;

    private Properties properties;
    @BeforeEach
    void beforeEach() {
        properties = ConsumerBuilder.getBuilder().build();
    }

    @AfterEach
    void afterEach() {
        consumer.close();
    }

    @Test
    public void test() {
        try {
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList("kwfarm"));
            while(true) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(100);
                consumerRecords.forEach(record -> {
                    logger.info("{}", record);
                });
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
