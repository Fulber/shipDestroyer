package com.dejava.shipDestroyer.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BattleshipCell {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int hp;
    private String symbol;

    public BattleshipCell() {
    }

    public BattleshipCell(int hp, String symbol) {
        this.hp = hp;
        this.symbol = symbol;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
