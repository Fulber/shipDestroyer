package com.dejava.shipDestroyer.model;

public class Shot {

    public enum STATUS {
        HIT, MISS, KILL
    }

    private int x;
    private int y;

    private STATUS status;

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

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
