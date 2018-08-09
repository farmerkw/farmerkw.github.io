package kr.kwfarm.study.kafka.sample.builder;

import kr.kwfarm.study.kafka.sample.builder.domain.AutoOffsetReset;
import kr.kwfarm.study.kafka.sample.builder.domain.Property;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class ConsumerBuilder extends AbstractBuilder {
    private Property<String> groupId = new Property<>(ConsumerConfig.GROUP_ID_CONFIG, StringSerializer.class.getName());

    private Property<Boolean> autoCommit = new Property<>(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.TRUE);

    private Property<AutoOffsetReset> offsetReset = new Property<>(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, AutoOffsetReset.latest);

    private Property<String> keyDeserializer = new Property<>(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    private Property<String> valueDeserializer = new Property<>(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    public static ConsumerBuilder getBuilder() {
        return new ConsumerBuilder();
    }

    public ConsumerBuilder groupId(String groupId) {
        this.groupId.setValue(groupId);
        return this;
    }


    public ConsumerBuilder autoCommit(Boolean autoCommit) {
        this.autoCommit.setValue(autoCommit);
        return this;
    }

    public ConsumerBuilder offsetReset(AutoOffsetReset offsetReset) {
        this.offsetReset.setValue(offsetReset);
        return this;
    }

    public ConsumerBuilder keyDeserializer(String keyDeserializer) {
        this.keyDeserializer.setValue(keyDeserializer);
        return this;
    }

    public ConsumerBuilder valueDeserializer(String valueDeserializer) {
        this.valueDeserializer.setValue(valueDeserializer);
        return this;
    }

}
