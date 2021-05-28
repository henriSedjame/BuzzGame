package io.gitlab.hsedjame.buzz.infrastructure;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public record GameState(
      AtomicBoolean started,
      int maxPlayers,
      int minPlayers,
      AtomicInteger numberOfPlayers,
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
        if (!players.contains(name)) players.add(name);

        if(!started.get())
            if (numberOfPlayers.get() < maxPlayers)
                if(numberOfPlayers.incrementAndGet() == maxPlayers)
                    start();

        return numberOfPlayers.get() >= minPlayers;
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
