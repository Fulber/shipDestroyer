package com.dejava.shipDestroyer.model;

import java.util.List;

public class BattleshipTemplate {

    private int width;
    private int height;

    private List<List<BattleshipCell>> canvas;

    public BattleshipTemplate() {
    }

    public BattleshipTemplate(int width, int height, List<List<BattleshipCell>> canvas) {
        this.width = width;
        this.height = height;
        this.canvas = canvas;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<List<BattleshipCell>> getCanvas() {
        return canvas;
    }

    public void setCanvas(List<List<BattleshipCell>> canvas) {
        this.canvas = canvas;
    }
}
