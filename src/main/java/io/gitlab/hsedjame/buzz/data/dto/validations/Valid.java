package io.gitlab.hsedjame.buzz.data.dto.validations;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Valid<T extends Validatable<T>> {
    private final T value;

    private Valid(T t) {
        this.value = t;
    }

    public static <T extends Validatable<T>> Valid<T> of(T request){
        return new Valid<>(request);
    }

    public T get(){
        return value;
    }

    public boolean isValid(){
        return value.getPredicates()
                .keySet()
                .stream()
                .allMatch(p -> p.test(value));
    }

    public List<String> validate() {

        if (isValid()) return Collections.emptyList();

        return value.getPredicates()
                .entrySet()
                .stream()
                .filter(entry -> !entry.getKey().test(value))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }
}
