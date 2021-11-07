package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.dto.States;
import io.gitlab.hsedjame.buzz.services.Emitters;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public record BuzzEventsController(Emitters emitters) {

    @GetMapping(value = "/states")
    public Flux<States.StateChange> states(){
        return emitters.stateChanges().asFlux();
    }

}
