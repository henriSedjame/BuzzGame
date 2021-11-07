package io.gitlab.hsedjame.buzz.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract sealed class Responses {

    @JsonProperty("type")
    abstract Type type();

    public non-sealed static class GameStarted extends Responses {
        @Override
        public Type type() {
            return Type.GAME_STARTED;
        }
    }

    public non-sealed static class PlayerAdded extends Responses {
        @Override
        public Type type() {
            return Type.PLAYER_ADDED;
        }
    }

    public non-sealed static class BuzzRegistered extends Responses {
        @Override
        public Type type() {
            return Type.BUZZ_REGISTERED;
        }
    }

    public non-sealed static class AnswerRegistered extends Responses {
        @Override
        public Type type() {
            return Type.ANSWER_REGISTERED;
        }
    }

    public non-sealed static class Error extends Responses {

        private final String message;

        public Error(String message){
           this.message = message;
        }

        @Override
        public Type type() {
            return Type.ERROR;
        }

        public String getMessage() {
            return message;
        }

    }

    enum Type {
        ERROR, GAME_STARTED, PLAYER_ADDED, BUZZ_REGISTERED, ANSWER_REGISTERED
    }
}
