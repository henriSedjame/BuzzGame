package io.gitlab.hsedjame.buzz.data.dto;

import java.util.List;

public sealed interface Messages {

    record Question(int number, String label, int points, List<Answer> answers) implements Messages{}

    record Answer(int number, String label, boolean good) implements Comparable<Answer>, Messages{
        @Override
        public int compareTo(Answer o) {
            return label.compareTo(o.label);
        }
    }

    record PlayerAnswer(String playerName, Answer answer) implements Messages{}

    record Buzz(String author) implements Messages{}

    record CanBuzz(boolean canBuzz) implements Messages {}

    record PlayerScore(String playerName, int score, String goodAnswer, boolean update) implements Messages{}

    record Error(String message) implements Messages {}
}
