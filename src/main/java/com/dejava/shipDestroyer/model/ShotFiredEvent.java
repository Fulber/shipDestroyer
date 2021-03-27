package com.dejava.shipDestroyer.model;

public class ShotFiredEvent {

    private String gameId;
    private String tournamentId;
    private int roundNo;
    private int x;
    private int y;

    public ShotFiredEvent() {
    }

    public ShotFiredEvent(String gameId, String tournamentId, int roundNo) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
        this.roundNo = roundNo;
    }

    public ShotFiredEvent(String gameId, String tournamentId, int roundNo, int x, int y) {
        this.gameId = gameId;
        this.tournamentId = tournamentId;
        this.roundNo = roundNo;
        this.x = x;
        this.y = y;
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

    public int getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(int roundNo) {
        this.roundNo = roundNo;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
