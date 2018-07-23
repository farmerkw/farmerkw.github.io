package kr.kwfarm.study.kafka.sample.builder;

import kr.kwfarm.study.kafka.sample.builder.domain.Property;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

public class ProducerBuilder extends AbstractBuilder {
    private Property<String> keySerializer = new Property<>(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    private Property<String> valueSerializer = new Property<>(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    private Property<String> acks = new Property<>(ProducerConfig.ACKS_CONFIG, "1");


    public static ProducerBuilder getBuilder() {
        return new ProducerBuilder();
    }

    public ProducerBuilder keySerializer(Class c) {
        keySerializer.setValue(c.getName());
        return this;
    }

    public ProducerBuilder valueSerializer(Class c) {
        valueSerializer.setValue(c.getName());
        return this;
    }

    public ProducerBuilder acks(String i) {
        acks.setValue(i);
        return this;
    }
}
