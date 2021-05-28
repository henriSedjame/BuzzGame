package io.gitlab.hsedjame.buzz.web;

import io.gitlab.hsedjame.buzz.data.db.PlayerRepository;
import io.gitlab.hsedjame.buzz.data.dto.Requests;
import io.gitlab.hsedjame.buzz.infrastructure.GameState;
import io.gitlab.hsedjame.buzz.services.BuzzService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


@Controller
public record BuzzViewController(BuzzService service, GameState state, PlayerRepository repository) {

    @GetMapping("/")
    public String getIndex(@RequestParam("player") String player) {

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repository.existsByName(player)
                        .subscribe(exist -> {
                            if(!exist) {
                                service.addPlayer(new Requests.AddPlayer(player))
                                        .subscribe();
                            }
                        });
            }
        }, 3000);


        return "index";
    }

}
