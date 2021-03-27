package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.*;
import com.dejava.shipDestroyer.model.BattleshipPlacement.DIRECTION;
import com.dejava.shipDestroyer.repository.GameBoardRepository;
import com.dejava.shipDestroyer.repository.GameStartedEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
public class Consumer {

    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public static final String GAME_STARTED_EVENT = "GameStartedEvent";

    @Autowired
    private RestService restService;
    @Autowired
    private GameStartedEventRepository gameStartedEventRepository;
    @Autowired
    private GameBoardRepository gameBoardRepository;

    @Value(value = "${tournamentId}")
    private String tournamentId;

    private final Random r = new Random();

    @KafkaListener(topics = "cc.battleships.game.started")
    public void gameStartedEvent(String message) throws IOException {
        GameStartedEvent event = new ObjectMapper().readValue(message, GameStartedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {
            logger.info("GAME START EVENT " + tournamentId + " - " + event.getGameId());

            gameStartedEventRepository.save(event);

            String[][] board = new String[event.getBattlegroundSize()][event.getBattlegroundSize()];
            String boardJson = new ObjectMapper().writeValueAsString(board);

            gameBoardRepository.save(new GameBoard(event.getGameId(), event.getTournamentId(), event.getBattlegroundSize(), boardJson));

            while (true) {
                try {
                    String token = restService.authenticate();

                    int x = r.nextInt(event.getBattlegroundSize());
                    int y = r.nextInt(event.getBattlegroundSize());
                    DIRECTION dir = DIRECTION.values()[r.nextInt(DIRECTION.values().length)];

                    BattleshipPlacement placement = new BattleshipPlacement(event.getGameId(), x, y, dir);
                    restService.registerShipToTournament(token, tournamentId, placement);

                    //TODO add misses in the board for our ship
                    logger.info("SHIP PLACED " + tournamentId + " - " + event.getGameId() + "[" + x + "," + y + "," + dir + "]");

                    break;
                } catch (Exception e) {
                    //TODO conflict & add possible hit
                    logger.error(e.getMessage());
                }
            }
        }
    }

//    @KafkaListener(topics = "cc.battleships.shot")
//    public void shotFiredEvent(String message) throws IOException {
//        logger.info(String.format("#### -> Consumed message -> %s", message));
//
//        ShotFiredEvent event = new ObjectMapper().readValue(message, ShotFiredEvent.class);
//
//        // TODO FIND HIT AND SHOOT
//        if (tournamentId.equals(event.getTournamentId())) {
//
//        }
//    }

    @KafkaListener(topics = "cc.battleships.round.started")
    public void roundStartedEvent(String message) throws IOException {
//        logger.info(String.format("#### -> Consumed message -> %s", message));

        RoundStartedEvent event = new ObjectMapper().readValue(message, RoundStartedEvent.class);

        //TODO shot here
    }

    @KafkaListener(topics = "cc.battleships.round.ended")
    public void roundEndedEvent(String message) throws IOException {
//        logger.info(String.format("#### -> Consumed message -> %s", message));

        RoundEndedEvent event = new ObjectMapper().readValue(message, RoundEndedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {
            for (Shot shot : event.getShots()) {
                switch (shot.getStatus()) {
                    case HIT:
                        break;
                    case MISS:
                        break;
                    case KILL:
                        break;
                }
            }
        }
    }
}