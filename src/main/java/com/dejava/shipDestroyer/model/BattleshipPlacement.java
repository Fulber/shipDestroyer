package com.dejava.shipDestroyer.model;

public class BattleshipPlacement {

    public enum DIRECTION {
        NORTH, SOUTH, WEST, EAST;
    }

    private String gameId;
    private int x;
    private int y;
    private DIRECTION direction;

    public BattleshipPlacement() {
    }

    public BattleshipPlacement(String gameId, int x, int y, DIRECTION direction) {
        this.gameId = gameId;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    public DIRECTION getDirection() {
        return direction;
    }

    public void setDirection(DIRECTION direction) {
        this.direction = direction;
    }
}
