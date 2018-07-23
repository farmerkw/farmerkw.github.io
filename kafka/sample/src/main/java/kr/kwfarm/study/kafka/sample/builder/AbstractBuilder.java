package kr.kwfarm.study.kafka.sample.builder;

import kr.kwfarm.study.kafka.sample.builder.domain.Property;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Stream;

public abstract class AbstractBuilder {
    protected Property<String> server = new Property<>("bootstrap.servers", "kafka-01:9092,kafka-02:9092,kafka-03:9092");

    public Properties build() {
        Properties properties = new Properties();

        Stream<Field> fields = Stream.concat(Arrays.stream(this.getClass().getSuperclass().getDeclaredFields()),
                Arrays.stream(this.getClass().getDeclaredFields()));
        fields.forEach(field -> {
            field.setAccessible(true);
            try {
                Property property = (Property) field.get(this);
                if (property.isValue()) {
                    properties.put(property.getKey(), String.valueOf(property.getValue()));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });

        return properties;
    }
}
