package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.GameStartedEvent;
import com.dejava.shipDestroyer.model.RoundEndedEvent;
import com.dejava.shipDestroyer.model.RoundStartedEvent;
import com.dejava.shipDestroyer.repository.GameStartedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static final String GAME_STARTED_EVENT = "GameStartedEvent";

    @Autowired
    private RestService restService;

    @Autowired
    private GameStartedEventRepository gameStartedEventRepository;

    @Value(value = "${tournamentId}")
    private String tournamentId;

    @KafkaListener(topics = "cc.battleships.game.started")
    public void gameStartedEvent(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        GameStartedEvent event = new ObjectMapper().readValue(message, GameStartedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {
            gameStartedEventRepository.save(event);

            Optional<GameStartedEvent> byId = gameStartedEventRepository.findById(event.getGameId());
        }
    }

    @KafkaListener(topics = "cc.battleships.round.started")
    public void roundStartedEvent(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        RoundStartedEvent event = new ObjectMapper().readValue(message, RoundStartedEvent.class);
    }

    @KafkaListener(topics = "cc.battleships.round.ended")
    public void roundEndedEvent(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));

        RoundEndedEvent event = new ObjectMapper().readValue(message, RoundEndedEvent.class);
    }
}