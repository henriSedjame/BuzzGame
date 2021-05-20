package io.gitlab.hsedjame.buzz.data.db;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository<Player, String> {

    Mono<Boolean> existsByName(String name);

    Mono<Player> findByName(String name);
}
