package io.gitlab.hsedjame.buzz.data.dto.validations;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.function.Predicate;

public interface Validatable<T extends Validatable<T>> {

        @JsonIgnore
        default Map<Predicate<T>, String> getPredicates() {
            return Map.of();
        }

}
