package com.dejava.shipDestroyer.model;

public class BattleshipCell {

    private int hp;
    private String symbol;

    public BattleshipCell() {
    }

    public BattleshipCell(int hp, String symbol) {
        this.hp = hp;
        this.symbol = symbol;
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
