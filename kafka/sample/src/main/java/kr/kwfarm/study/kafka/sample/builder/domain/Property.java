package kr.kwfarm.study.kafka.sample.builder.domain;

import java.util.Optional;

public class Property<T> {
    private String key;

    private Optional<T> value;

    public Property(String key, T value) {
        this.key = key;
        this.value = Optional.of(value);
    }

    public void setValue(T t) {
        value = Optional.ofNullable(t);
    }

    public boolean isValue() {
        return value.isPresent();
    }

    public T getValue() {
        return value.get();
    }

    @Override
    public String toString() {
        return "Property{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }

    public String getKey() {
        return key;
    }
}
