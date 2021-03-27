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
    private Producer producer;
    @Autowired
    private RestService restService;
    @Autowired
    private ProcessingService processing;
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
                    BattleshipPlacement placement = randomBattleshipPlacement(event.getGameId(), event.getBattlegroundSize());

                    restService.registerShipToTournament(tournamentId, placement);

                    logger.info("SHIP PLACED " + tournamentId + " - " + event.getGameId() + "[" + placement + "]");

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

        if (tournamentId.equals(event.getTournamentId())) {
            logger.info("ROUND STARTED EVENT " + tournamentId + " - " + event.getGameId() + " - " + event.getRoundNo());

            GameBoard board = gameBoardRepository.findById(event.getGameId()).get();

            ShotFiredEvent shot = new ShotFiredEvent(event.getGameId(), tournamentId, event.getRoundNo());
            if (board.getNextPossibleShot() == null) {
                shot.setX(r.nextInt(board.getBattlegroundSize()));
                shot.setY(r.nextInt(board.getBattlegroundSize()));
            } else {
                shot.setX(board.getNextPossibleShot().getX());
                shot.setY(board.getNextPossibleShot().getY());
            }

            producer.shoot(shot);
        }
    }

    @KafkaListener(topics = "cc.battleships.round.ended")
    public void roundEndedEvent(String message) throws IOException {
        RoundEndedEvent event = new ObjectMapper().readValue(message, RoundEndedEvent.class);

        if (tournamentId.equals(event.getTournamentId())) {

            GameBoard board = gameBoardRepository.findById(event.getGameId()).get();
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
        var canvas = event.getBattleshipTemplate().getCanvas();
        DIRECTION direction = placement.getDirection();
        int x = placement.getX();
        int y = placement.getY();
        int canvasHeight, canvasWidth;
        switch (placement.getDirection()) {
            case NORTH:
                canvasHeight = canvas.length;
                canvasWidth = canvas[0].length;
                for(int canvasY = 0; canvasY < canvasHeight; canvasY ++) {
                    for(int canvasX = 0; canvasX < canvasWidth; canvasX ++) {
                        board[y + canvasY][x + canvasX] = canvas[canvasY][canvasX].getHp() > 0 ? "MISS" : null;
                    }
                }
                break;
            case EAST:
                canvas = processing.rotateClockWise(canvas);
                canvasHeight = canvas.length;
                canvasWidth = canvas[0].length;
                for(int canvasY = 0; canvasY < canvasHeight; canvasY ++) {
                    for(int canvasX = 0; canvasX < canvasWidth; canvasX ++) {
                        board[y + canvasY][x + canvasX] = canvas[canvasY][canvasX].getHp() > 0 ? "MISS" : null;
                    }
                }
                break;
            case SOUTH:
                canvas = processing.rotateClockWise(canvas);
                canvas = processing.rotateClockWise(canvas);
                canvasHeight = canvas.length;
                canvasWidth = canvas[0].length;
                for(int canvasY = 0; canvasY < canvasHeight; canvasY ++) {
                    for(int canvasX = 0; canvasX < canvasWidth; canvasX ++) {
                        board[y - canvasY][x - canvasX] = canvas[canvasY][canvasX].getHp() > 0 ? "MISS" : null;
                    }
                }
                break;
            case WEST:
                canvas = processing.rotateClockWise(canvas);
                canvas = processing.rotateClockWise(canvas);
                canvas = processing.rotateClockWise(canvas);
                canvasHeight = canvas.length;
                canvasWidth = canvas[0].length;
                for(int canvasY = 0; canvasY < canvasHeight; canvasY ++) {
                    for(int canvasX = 0; canvasX < canvasWidth; canvasX ++) {
                        board[y - canvasY][x - canvasX] = canvas[canvasY][canvasX].getHp() > 0 ? "MISS" : null;
                    }
                }
                break;
        }
        return board;
    }

    private BattleshipPlacement randomBattleshipPlacement(String gameId, int size) {
        DIRECTION dir = DIRECTION.values()[r.nextInt(DIRECTION.values().length)];
        return new BattleshipPlacement(gameId, r.nextInt(size), r.nextInt(size), dir);
    }
}