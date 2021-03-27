package com.dejava.shipDestroyer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;

@Entity
public class GameBoard {

    @Id
    private String gameId;

    private String tournamentId;
    private Integer battlegroundSize;

    @Transient
    private String[][] board;

    @Lob
    @Column
    private String boardJson;

    @OneToOne(cascade = CascadeType.ALL)
    private Shot lastHit;

    @OneToOne(cascade = CascadeType.ALL)
    private Shot nextPossibleShot;

    public GameBoard() {
    }

    public GameBoard(String gameId, String tournamentId, Integer battlegroundSize, String boardJson) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
        this.battlegroundSize = battlegroundSize;
        this.boardJson = boardJson;
    }

    public GameBoard(String gameId, String tournamentId, Integer battlegroundSize, String[][] board, String boardJson, Shot lastHit, Shot nextPossibleShot) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
        this.battlegroundSize = battlegroundSize;
        this.board = board;
        this.boardJson = boardJson;
        this.lastHit = lastHit;
        this.nextPossibleShot = nextPossibleShot;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(String tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Integer getBattlegroundSize() {
        return battlegroundSize;
    }

    public void setBattlegroundSize(Integer battlegroundSize) {
        this.battlegroundSize = battlegroundSize;
    }

    public String[][] getBoard() {
        try {
            this.board = new ObjectMapper().readValue(boardJson, String[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return board;
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public String getBoardJson() {
        return boardJson;
    }

    public void setBoardJson(String boardJson) {
        this.boardJson = boardJson;
    }

    public Shot getLastHit() {
        return lastHit;
    }

    public void setLastHit(Shot lastHit) {
        this.lastHit = lastHit;
    }

    public Shot getNextPossibleShot() {
        return nextPossibleShot;
    }

    public void setNextPossibleShot(Shot nextPossibleShot) {
        this.nextPossibleShot = nextPossibleShot;
    }
}
