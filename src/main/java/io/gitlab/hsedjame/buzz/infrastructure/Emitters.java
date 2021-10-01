package io.gitlab.hsedjame.buzz.infrastructure;

import io.gitlab.hsedjame.buzz.data.db.Player;
import io.gitlab.hsedjame.buzz.data.dto.Messages;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.*;
import java.util.function.BiFunction;

import static io.gitlab.hsedjame.buzz.services.Utils.*;

public record Emitters(
        Sinks.Many<Messages.StateChange> stateChanges,
        List<Messages.Question> questionList,
        Iterator<Messages.Question> questionsIterator
) {

    public void gameStart(List<String> players) {
        stateChanges.tryEmitNext(Messages.StateChange.start(players));
        enableBuzz();
        nextQuestion();
    }

    public void emitScore(Player player, String goodAnswer, boolean update, List<String> players, int nb) {
        stateChanges.tryEmitNext(Messages.StateChange.withScore(
                new Messages.PlayerScore(player.name(), player.score(), goodAnswer, update), players, nb));
    }

    public void nextQuestion() {
        if (questionsIterator.hasNext())
            stateChanges.tryEmitNext(Messages.StateChange.withQuestion(questionsIterator().next()));
        else
            stateChanges.tryEmitNext(Messages.StateChange.end());
    }

    public void registerBuzz(Requests.Buzz buzz) {
        disableBuzz();
        stateChanges.tryEmitNext(
                Messages.StateChange.withBuzz(new Messages.Buzz(buzz.playerName()))
        );
    }

    public void registerAnswer(Requests.Answer answer, Player player, BiFunction<Player, Integer, Mono<Player>> updatePlayerScore, GameState state) {
        questionList.stream()
                .filter(q -> q.number() == answer.questionNumber())
                .findFirst()
                .ifPresent(q -> {
                    consumeWith(() -> q.answers().stream().filter(Messages.Answer::good).findFirst().get(), goodAnswer ->

                                    consumeWith(() -> goodAnswer.number() == answer.answerNumber(), good -> {

                                                stateChanges.tryEmitNext(Messages.StateChange.withAnswer(
                                                        new Messages.PlayerAnswer(player.name(), new Messages.Answer(0, goodAnswer.label(), good))));

                                                if (good) updatePlayerScore
                                                        .apply(player, q.points())
                                                        .subscribe(p -> emitScore(p, null, true, state.players(), state.minPlayers()));

                                                else
                                                    emitScore(player, q.answers().stream().filter(Messages.Answer::good).map(Messages.Answer::label).findFirst().get(), true, state.players(), state.minPlayers());

                                                waitAndApply(() -> {
                                                    enableBuzz();
                                                    nextQuestion();
                                                }, 1000);

                                            })

                    );
                    /*Messages.Answer goodAnswer = q.answers().stream().filter(Messages.Answer::good).findFirst().get();
                    boolean good = goodAnswer.number() == answer.answerNumber();

                    stateChanges.tryEmitNext(Messages.StateChange.withAnswer(
                            new Messages.PlayerAnswer(player.name(), new Messages.Answer(0, goodAnswer.label(), good))));

                    if (good) updatePlayerScore
                            .apply(player, q.points())
                            .subscribe(p -> emitScore(p, null, true, state.players(), state.minPlayers()));

                    else emitScore(player, q.answers().stream().filter(Messages.Answer::good).map(Messages.Answer::label).findFirst().get(), true, state.players(), state.minPlayers());

                    waitAndApply(()->{
                        enableBuzz();
                        nextQuestion();
                    }, 1000);*/

                });

    }

    public void disableBuzz() {
        stateChanges.tryEmitNext(Messages.StateChange.withCanBuzz(false));
    }

    public void enableBuzz() {
        stateChanges.tryEmitNext(Messages.StateChange.withCanBuzz(true));
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
