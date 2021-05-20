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
        Sinks.One<Boolean> gameCanStart,
        Sinks.One<Boolean> gameCanEnd,
        Sinks.Many<Messages.PlayerScore> playerScores,
        Sinks.Many<Messages.Question> questions,
        Sinks.Many<Messages.Buzz> buzzes,
        Sinks.Many<Messages.PlayerAnswer> answers,
        List<Messages.Question> questionList,
        Iterator<Messages.Question> questionsIterator
) {

    public void enableStartingGame(){

        gameCanStart.emitValue(Boolean.TRUE, ((signalType, emitResult) -> {

            var retry = switch (emitResult){

                case OK, FAIL_TERMINATED, FAIL_OVERFLOW , FAIL_CANCELLED, FAIL_NON_SERIALIZED-> { yield false; }

                case FAIL_ZERO_SUBSCRIBER -> { yield true; }
            };

            return signalType.equals(SignalType.SUBSCRIBE) && retry;
        }));
    }

    public void emitScore(String player, int score, Messages.AfterScoreActions action) {
        playerScores.tryEmitNext(new Messages.PlayerScore(player, score, action));
    }

    public void nextQuestion(){
        if(questionsIterator.hasNext())
            questions.tryEmitNext(questionsIterator().next());

        gameCanEnd.tryEmitValue(Boolean.TRUE);
    }

    public void registerBuzz(Requests.Buzz buzzz){
        buzzes.tryEmitNext(new Messages.Buzz(buzzz.playerName(), LocalDateTime.now()));
    }

    public void registerAnswer(Requests.Answer answer, Player player, BiFunction<Player, Integer, Mono<Player>> updatePlayerScore) {

        questionList.stream()
                .filter(q -> q.number() == answer.questionNumber())
                .findFirst()
                .ifPresent(q -> {
                    boolean b = q.answers().stream().anyMatch(a -> a.good() && a.number() == answer.answerNumber());
                    if (b) {
                        updatePlayerScore.apply(player, q.points())
                            .doOnNext(p -> {
                                emitScore(p.name(), p.score(), Messages.AfterScoreActions.NEXT_QUESTION);

                                Timer timer = new Timer();

                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        nextQuestion();
                                        timer.cancel();
                                    }
                                }, 2000);
                            });


                    } else {
                        emitScore(answer.playerName(), 0, Messages.AfterScoreActions.NONE);
                    }


                });

    }

}
