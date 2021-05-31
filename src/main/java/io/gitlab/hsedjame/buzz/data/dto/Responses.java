package io.gitlab.hsedjame.buzz.data.dto;

public sealed interface Responses {

    Type type();

    record GameStarted(Type type) implements Responses {
        public GameStarted(){
            this(Type.GAME_STARTED);
        }
    }
    record PlayerAdded(Type type) implements Responses {
        public PlayerAdded(){
            this(Type.PLAYER_ADDED);
        }
    }
    record BuzzRegistered(Type type) implements Responses {
        public BuzzRegistered(){
            this(Type.BUZZ_REGISTERED);
        }
    }
    record AnswerRegistered(Type type) implements Responses {
        public AnswerRegistered(){
            this(Type.ANSWER_REGISTERED);
        }
    }
    record Error(Type type, String message) implements Responses {
        public Error(String message){
            this(Type.ERROR, message);
        }
    }

    enum Type {
        ERROR, GAME_STARTED, PLAYER_ADDED, BUZZ_REGISTERED, ANSWER_REGISTERED
    }
}
