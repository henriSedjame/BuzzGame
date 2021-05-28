package io.gitlab.hsedjame.buzz.data.dto;

import java.util.List;

public class Messages {
    public static record Question(int number, String label, int points, List<Answer> answers){}
    public static record Answer(int number, String label, boolean good) implements Comparable<Answer>{
        @Override
        public int compareTo(Answer o) {
            return label.compareTo(o.label);
        }
    }
    public static record PlayerAnswer(String playerName, Answer answer){}
    public static record Buzz(String author){}
    public static record PlayerScore(String playerName, int score, String goodAnswer, boolean update){}

    public static record StateChange(
            StateChangeType type,
            boolean canBuzz,
            PlayerScore playerScore,
            Question question,
            Buzz buzz,
            PlayerAnswer answer,
            List<String> players,
            int requiredNbPlayers
    ){
        public static StateChange start(List<String> players){
            return new StateChange(StateChangeType.GAME_START, false, null, null, null, null, players, 0);
        }

        public static StateChange end(){
            return new StateChange(StateChangeType.GAME_END, false, null, null, null, null, null,0);
        }

        public static StateChange withCanBuzz(boolean b){
            return new StateChange(StateChangeType.CAN_BUZZ, b, null, null, null, null, null,0);
        }

        public static StateChange withScore(PlayerScore score, List<String> players, int requiredNbPlayers){
            return new StateChange(StateChangeType.NEW_PLAYER_SCORE, false, score, null, null, null, players,requiredNbPlayers);
        }
        public static StateChange withQuestion(Question question){
            return new StateChange(StateChangeType.NEW_QUESTION, false, null, question, null, null, null,0);
        }
        public static StateChange withBuzz(Buzz buzz){
            return new StateChange(StateChangeType.NEW_BUZZ, false, null, null, buzz, null, null,0);
        }
        public static StateChange withAnswer(PlayerAnswer answer){
            return new StateChange(StateChangeType.NEW_ANSWER, false, null, null, null, answer, null,0);
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
