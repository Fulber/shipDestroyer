package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.GameStartedEvent;
import com.dejava.shipDestroyer.model.RoundEndedEvent;
import com.dejava.shipDestroyer.model.RoundStartedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static final String GAME_STARTED_EVENT = "GameStartedEvent";

    @Autowired
    private RestService restService;

    @Value(value = "${tournamentId}")
    private String tournamentId;

    @KafkaListener(topics = "cc.battleships.game.started")
    public void gameStartedEvent(GameStartedEvent event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event));

    }

    @KafkaListener(topics = "cc.battleships.round.started")
    public void roundStartedEvent(RoundStartedEvent event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event));

    }

    @KafkaListener(topics = "cc.battleships.round.ended")
    public void roundEndedEvent(RoundEndedEvent event) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", event));

    }
}