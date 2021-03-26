package com.dejava.shipDestroyer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    public static final String GameStartedEvent = "GameStartedEvent";

    @KafkaListener(topics = " cc.battleships.game.started", groupId = "group_id")
    public void gameStartedEvent(String message) throws IOException {
        logger.info(String.format("#### -> Consumed message -> %s", message));
    }
}