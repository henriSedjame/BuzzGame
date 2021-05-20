package io.gitlab.hsedjame.buzz.infrastructure;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public record GameState(
      AtomicBoolean started,
      int maxPlayers,
      int minPlayers,
      AtomicInteger numberOfPlayers
) {

    public void start(){
        if (numberOfPlayers.get() >= minPlayers && !started.get())
            started.set(true);
    }

    public void stop(){
        started.set(false);
    }

    public boolean addPlayer(){
        if (numberOfPlayers.get() < maxPlayers)
            if(numberOfPlayers.incrementAndGet() == maxPlayers)
                start();

        return numberOfPlayers.get() >= minPlayers;
    }

}
