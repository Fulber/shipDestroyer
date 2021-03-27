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

            while (true) {
                try {
                    String token = restService.authenticate();

                    int x = r.nextInt(event.getBattlegroundSize());
                    int y = r.nextInt(event.getBattlegroundSize());
                    DIRECTION dir = DIRECTION.values()[r.nextInt(DIRECTION.values().length)];

                    BattleshipPlacement placement = new BattleshipPlacement(event.getGameId(), x, y, dir);
                    restService.registerShipToTournament(token, tournamentId, placement);

                    logger.info("SHIP PLACED " + tournamentId + " - " + event.getGameId() + "[" + x + "," + y + "," + dir + "]");

                    String boardJson = new ObjectMapper().writeValueAsString(generateBoardWithPlacement(event, placement));
                    gameBoardRepository.save(new GameBoard(event.getGameId(), event.getTournamentId(), event.getBattlegroundSize(), boardJson));

                    break;
                } catch (Exception e) {
                    //TODO conflict & add possible hit
                    logger.error(e.getMessage());
                }
            }
        }
    }

    @KafkaListener(topics = "cc.battleships.game.ended")
    public void gameEndedEvent(String message) throws IOException {
        GameEndedEvent event = new ObjectMapper().readValue(message, GameEndedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {
            logger.info("GAME ENDED EVENT " + tournamentId + " - " + event.getGameId());

            gameStartedEventRepository.deleteById(event.getGameId());
            gameBoardRepository.deleteById(event.getGameId());
        }
    }

    @KafkaListener(topics = "cc.battleships.round.started")
    public void roundStartedEvent(String message) throws IOException {
        RoundStartedEvent event = new ObjectMapper().readValue(message, RoundStartedEvent.class);

        //TODO shot here
        if (tournamentId.equals(event.getTournamentId())) {
            logger.info("ROUND STARTED EVENT " + tournamentId + " - " + event.getGameId() + " - " + event.getRoundNo());
        }
    }

    @KafkaListener(topics = "cc.battleships.round.ended")
    public void roundEndedEvent(String message) throws IOException {
        RoundEndedEvent event = new ObjectMapper().readValue(message, RoundEndedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {

            GameBoard board = gameBoardRepository.findById(event.getGameId()).orElseGet(null);
            String[][] boardMatrix = board.getBoard();

            for (Shot shot : event.getShots()) {
                if (boardMatrix[shot.getX()][shot.getY()] == null) {
                    switch (shot.getStatus()) {
                        case HIT:
                            boardMatrix[shot.getX()][shot.getY()] = "HIT";
                            board.setLastHit(shot);
                            break;
                        case MISS:
                            boardMatrix[shot.getX()][shot.getY()] = "MISS";
                            break;
                        case KILL:
                            boardMatrix[shot.getX()][shot.getY()] = "KILL";
                            //TODO restrict some
                            break;
                    }
                }
            }

            String boardJson = new ObjectMapper().writeValueAsString(boardMatrix);
            board.setBoardJson(boardJson);

            gameBoardRepository.save(board);
        }
    }

    private String[][] generateBoardWithPlacement(GameStartedEvent event, BattleshipPlacement placement) {
        String[][] board = new String[event.getBattlegroundSize()][event.getBattlegroundSize()];


        return board;
    }
}