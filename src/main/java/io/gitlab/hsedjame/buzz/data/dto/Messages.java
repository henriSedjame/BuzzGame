package io.gitlab.hsedjame.buzz.data.dto;

import java.time.LocalDateTime;
import java.util.TreeSet;

public class Messages {
    public static record Question(int number, String label, int points, TreeSet<Answer> answers){}
    public static record Answer(int number, String label, boolean good) implements Comparable<Answer>{
        @Override
        public int compareTo(Answer o) {
            return label.compareTo(o.label);
        }
    }
    public static record PlayerAnswer(String playerName, Answer answer){}
    public static record Buzz(String author, LocalDateTime time){}
    public static record PlayerScore(String playerName, int score, String goodAnswer){}
}
