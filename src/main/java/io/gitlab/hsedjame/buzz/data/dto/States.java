package io.gitlab.hsedjame.buzz.data.dto;

import java.util.List;

public class States {
    public record StateChange(
            States.StateChangeType type,
            Messages message,
            List<String> players,
            int requiredNbPlayers
    ){

        public static States.StateChange start(List<String> players){
            return new States.StateChange(States.StateChangeType.GAME_START,  null, players, 0);
        }

        public static States.StateChange end(){
            return new States.StateChange(States.StateChangeType.GAME_END,  null,  null,0);
        }

        public static States.StateChange withCanBuzz(boolean b){
            return new States.StateChange(States.StateChangeType.CAN_BUZZ,   new Messages.CanBuzz(b), null,0);
        }

        public static States.StateChange withScore(Messages.PlayerScore score, List<String> players, int requiredNbPlayers){
            return new States.StateChange(States.StateChangeType.NEW_PLAYER_SCORE,  score,  players, requiredNbPlayers);
        }

        public static States.StateChange withQuestion(Messages.Question question){
            return new States.StateChange(States.StateChangeType.NEW_QUESTION,   question,  null,0);
        }

        public static States.StateChange withBuzz(Messages.Buzz buzz){
            return new States.StateChange(States.StateChangeType.NEW_BUZZ,  buzz, null,0);
        }

        public static States.StateChange withAnswer(Messages.PlayerAnswer answer){
            return new States.StateChange(States.StateChangeType.NEW_ANSWER,   answer, null,0);
        }

        public static States.StateChange withError(String message) {
            return new States.StateChange(StateChangeType.ERROR, new Messages.Error(message), null, 0);
        }

    }

    public enum StateChangeType {
        GAME_START,
        GAME_END,
        CAN_BUZZ,
        NEW_PLAYER_SCORE,
        NEW_QUESTION,
        NEW_BUZZ,
        NEW_ANSWER,
        ERROR
    }
}
