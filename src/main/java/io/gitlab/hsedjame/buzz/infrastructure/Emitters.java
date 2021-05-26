package io.gitlab.hsedjame.buzz.infrastructure;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiFunction;

public record Emitters(
        Sinks.One<Boolean> startGame,
        Sinks.One<Boolean> endGame,
        Sinks.Many<Boolean> canBuzz,
        Sinks.Many<Messages.PlayerScore> playerScores,
        Sinks.Many<Messages.Question> questions,
        Sinks.Many<Messages.Buzz> buzzes,
        Sinks.Many<Messages.PlayerAnswer> answers,
        List<Messages.Question> questionList,
        Iterator<Messages.Question> questionsIterator
) {

    public void gameStart() {

        startGame.emitValue(Boolean.TRUE, ((signalType, emitResult) -> {

            var retry = switch (emitResult) {

                case OK, FAIL_TERMINATED, FAIL_OVERFLOW, FAIL_CANCELLED, FAIL_NON_SERIALIZED -> {
                    yield false;
                }

                case FAIL_ZERO_SUBSCRIBER -> {
                    yield true;
                }
            };

            return signalType.equals(SignalType.SUBSCRIBE) && retry;
        }));

        enableBuzz();
        nextQuestion();
    }

    public void emitScore(Player player, String goodAnswer) {
        playerScores.tryEmitNext(new Messages.PlayerScore(player.name(), player.score(), goodAnswer));
    }

    public void nextQuestion() {
        if (questionsIterator.hasNext())
            questions.tryEmitNext(questionsIterator().next());

        endGame.tryEmitValue(Boolean.TRUE);
    }

    public void registerBuzz(Requests.Buzz buzzz) {
        disableBuzz();
        buzzes.tryEmitNext(new Messages.Buzz(buzzz.playerName(), LocalDateTime.now()));
    }

    public void registerAnswer(Requests.Answer answer, Player player, BiFunction<Player, Integer, Mono<Player>> updatePlayerScore) {

        questionList.stream()
                .filter(q -> q.number() == answer.questionNumber())
                .findFirst()
                .ifPresent(q -> {
                    boolean b = q.answers().stream().anyMatch(a -> a.good() && a.number() == answer.answerNumber());
                    if (b) updatePlayerScore
                            .apply(player, q.points())
                            .doOnNext(p -> emitScore(p, null));

                    else emitScore(player, q.answers().stream().filter(Messages.Answer::good).map(Messages.Answer::label).findFirst().get());

                    enableBuzz();

                    Timer timer = new Timer();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            nextQuestion();
                            timer.cancel();
                        }
                    }, 1000);
                });

    }

    public void disableBuzz() {
        canBuzz.tryEmitNext(false);
    }

    public void enableBuzz() {
        canBuzz.tryEmitNext(true);
    }
}
