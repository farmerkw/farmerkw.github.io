package kr.kwfarm.study.kafka.sample.builder;

import kr.kwfarm.study.kafka.sample.builder.domain.AutoOffsetReset;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class ProducerBuilderTest {
    @Test
    public void defaultValue() {
        Properties properties = ProducerBuilder.getBuilder().build();

        assertThat(properties.getProperty("bootstrap.servers"), is("kafka-01:9092,kafka-02:9092,kafka-03:9092"));
        assertThat(properties.getProperty("acks"), is("1"));
        assertThat(properties.getProperty("value.serializer"), is("org.apache.kafka.common.serialization.StringSerializer"));
        assertThat(properties.getProperty("key.serializer"), is("org.apache.kafka.common.serialization.StringSerializer"));
    }

    @Test
    public void test() {
        Properties properties = ProducerBuilder.getBuilder().acks("all").build();

        assertThat(properties.getProperty("bootstrap.servers"), is("kafka-01:9092,kafka-02:9092,kafka-03:9092"));
        assertThat(properties.getProperty("acks"), is("all"));
        assertThat(properties.getProperty("value.serializer"), is("org.apache.kafka.common.serialization.StringSerializer"));
        assertThat(properties.getProperty("key.serializer"), is("org.apache.kafka.common.serialization.StringSerializer"));
    }
}