package io.gitlab.hsedjame.buzz.infrastructure;

import io.gitlab.hsedjame.buzz.data.dto.Messages;
import org.springframework.data.util.Pair;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public record GameInfo(
      AtomicBoolean started,
      int maxPlayers,
      int minPlayers,
      AtomicInteger numberOfPlayers,
      AtomicReference<Pair<Messages.Question, Messages.Answer>> currentQuestion,
      AtomicBoolean buzzed,
      List<String> players
) {

    public void start(){
        if (numberOfPlayers.get() >= minPlayers && !started.get())
            started.set(true);
    }

    public void stop(){
        started.set(false);
    }

    public boolean addPlayer(String name){
        if ( !started.get() && !players.contains(name)) {
            players.add(name);
            numberOfPlayers.incrementAndGet();
        }

        return numberOfPlayers.get() >= minPlayers;
    }

    public void setCurrentQuestion(Messages.Question q, Messages.Answer a) {
        currentQuestion.set(Pair.of(q, a));
    }

    public Mono<Boolean> addBuzz(){
        if (!buzzed.get()){
            buzzed.set(true);
            return Mono.just(true);
        }

        return Mono.just(false);
    }

    public Mono<Boolean> releaseBuzz(){
        buzzed.set(false);
        return Mono.just(true);
    }
}
