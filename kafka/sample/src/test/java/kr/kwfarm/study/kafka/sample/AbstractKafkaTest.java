package kr.kwfarm.study.kafka.sample;

import java.util.Properties;

public abstract class AbstractKafkaTest {
    protected Properties getDefaultProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.server", "kafka-01:9092,kafka-02:9092,kafka-03:9092");
        return properties;
    }
}
