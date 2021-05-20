package io.gitlab.hsedjame.buzz.data.dto.validations;

import java.util.Map;
import java.util.function.Predicate;

public interface Validatable<T extends Validatable<T>> {

        default Map<Predicate<T>, String> getPredicates() {
            return Map.of();
        }

}
