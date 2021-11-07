package io.gitlab.hsedjame.buzz.data.dto;

import java.util.List;

public class States {
    public record StateChange(
            States.StateChangeType type,
            boolean canBuzz,
            Messages message,
            List<String> players,
            int requiredNbPlayers
    ){

        public static States.StateChange start(List<String> players){
            return new States.StateChange(States.StateChangeType.GAME_START, false, null, players, 0);
        }

        public static States.StateChange end(){
            return new States.StateChange(States.StateChangeType.GAME_END, false, null,  null,0);
        }

        public static States.StateChange withCanBuzz(boolean b){
            return new States.StateChange(States.StateChangeType.CAN_BUZZ, b, null, null,0);
        }

        public static States.StateChange withScore(Messages.PlayerScore score, List<String> players, int requiredNbPlayers){
            return new States.StateChange(States.StateChangeType.NEW_PLAYER_SCORE, false, score,  players, requiredNbPlayers);
        }

        public static States.StateChange withQuestion(Messages.Question question){
            return new States.StateChange(States.StateChangeType.NEW_QUESTION, false,  question,  null,0);
        }

        public static States.StateChange withBuzz(Messages.Buzz buzz){
            return new States.StateChange(States.StateChangeType.NEW_BUZZ, false,  buzz, null,0);
        }

        public static States.StateChange withAnswer(Messages.PlayerAnswer answer){
            return new States.StateChange(States.StateChangeType.NEW_ANSWER, false,  answer, null,0);
        }
    }

    public enum StateChangeType {
        GAME_START,
        GAME_END,
        CAN_BUZZ,
        NEW_PLAYER_SCORE,
        NEW_QUESTION,
        NEW_BUZZ,
        NEW_ANSWER
    }
}
