package io.gitlab.hsedjame.buzz.services.dto;

public sealed interface Responses {
    record PlayerAdded() implements Responses {}
}
