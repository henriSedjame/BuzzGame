package io.gitlab.hsedjame.buzz.data.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("Players")
public record Player(@Id String id, String name, int score) {
    public static Player withName(String name){
        return new Player(UUID.randomUUID().toString(), name, 0);
    }
}
