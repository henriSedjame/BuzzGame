package io.gitlab.hsedjame.buzz.services;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.data.dto.States;
import io.gitlab.hsedjame.buzz.infrastructure.GameInfo;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.*;
import java.util.function.BiFunction;

import static io.gitlab.hsedjame.buzz.services.Utils.*;

public record Emitters(
        Sinks.Many<States.StateChange> stateChanges,
        Iterator<Messages.Question> questionsIterator
) {

    public void onGameStarted(GameInfo gameInfo) {
        gameInfo.start();
        stateChanges.tryEmitNext(States.StateChange.start(gameInfo.players()));
        enableBuzz();
        nextQuestion(gameInfo);
    }

    public void emitScore(Player player, String goodAnswer, boolean update, List<String> players, int nb) {
        stateChanges.tryEmitNext(States.StateChange.withScore(
                new Messages.PlayerScore(player.name(), player.score(), goodAnswer, update), players, nb));
    }

    public void onBuzzRegistered(Requests.Buzz buzz) {
        disableBuzz();
        stateChanges.tryEmitNext(
                States.StateChange.withBuzz(new Messages.Buzz(buzz.playerName()))
        );
    }

    public void onAnswerRegistered(Requests.Answer answer, Player player, BiFunction<Player, Integer, Mono<Player>> updatePlayerScore, GameInfo gameInfo) {

        consumeWith(
                () -> gameInfo.currentQuestion().get(),

                pair -> {
                    Messages.Question question = pair.getFirst();
                    Messages.Answer goodAnswer = pair.getSecond();

                    consumeWith(() -> goodAnswer.number() == answer.answerNumber(), good -> {

                        stateChanges.tryEmitNext(States.StateChange.withAnswer(
                                new Messages.PlayerAnswer(answer.playerName(), new Messages.Answer(0, goodAnswer.label(), good))));

                        if (good)
                            updatePlayerScore
                                    .apply(player, question.points())
                                    .subscribe(p -> emitScore(p, null, true, gameInfo.players(), gameInfo.minPlayers()));
                        else
                            emitScore(player, goodAnswer.label(), false, gameInfo.players(), gameInfo.minPlayers());

                        waitAndApply(() -> {
                            enableBuzz();
                            nextQuestion(gameInfo);
                        }, 1000);

                    });
                }
        );

    }

    private void nextQuestion(GameInfo gameInfo) {
        if (questionsIterator.hasNext()) {
            Messages.Question question = questionsIterator().next();

            stateChanges.tryEmitNext(States.StateChange.withQuestion(question));

            question.answers().stream()
                    .filter(Messages.Answer::good)
                    .findFirst()
                    .ifPresent(gAnswer -> gameInfo.setCurrentQuestion(question, gAnswer));


        } else
            stateChanges.tryEmitNext(States.StateChange.end());

    }

    private void disableBuzz() {
        stateChanges.tryEmitNext(States.StateChange.withCanBuzz(false));
    }

    private void enableBuzz() {
        stateChanges.tryEmitNext(States.StateChange.withCanBuzz(true));
    }

    private void waitAndApply(Runnable runnable, long delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
                timer.cancel();
            }
        }, delay);
    }
}
