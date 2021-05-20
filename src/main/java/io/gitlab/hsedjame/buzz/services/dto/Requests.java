package io.gitlab.hsedjame.buzz.services.dto;

import org.apache.logging.log4j.util.Strings;

import java.util.Map;
import java.util.function.Predicate;

import io.gitlab.hsedjame.buzz.services.dto.validations.Validatable;

public sealed interface Requests<T extends Validatable<T>> extends Validatable<T> {

    record AddPlayer(String name) implements Requests<AddPlayer> {

        @Override
        public Map<Predicate<AddPlayer>, String> getPredicates() {
            return Map.of(r -> Strings.isNotBlank(r.name()), "Name is required");
        }
    }

    record Buzz(String playerName) implements Requests<Buzz> {

    }

    record Answer(int nQuestion, String answer) implements Requests<Answer> { }
}
