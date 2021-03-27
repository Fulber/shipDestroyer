package com.dejava.shipDestroyer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Shot {

    public enum STATUS {
        HIT, MISS, KILL
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int x;
    private int y;

    private STATUS status;

    public Shot() {
    }

    public Shot(int x, int y, STATUS status) {
        this.x = x;
        this.y = y;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
