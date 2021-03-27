package com.dejava.shipDestroyer.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;

@Entity
public class BattleshipTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int width;
    private int height;

    @Transient
    private BattleshipCell[][] canvasList;

    private String canvas;

    public BattleshipTemplate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BattleshipCell[][] getCanvasList() {
        return canvasList;
    }

    public void setCanvasList(BattleshipCell[][] canvasList) {
        this.canvasList = canvasList;
    }

    public String getCanvas() {
        return canvas;
    }

    public void setCanvas(String canvas) {
        this.canvas = canvas;

        try {
            BattleshipCell[][] battleshipCells = new ObjectMapper().readValue(canvas, BattleshipCell[][].class);

            setCanvasList(battleshipCells);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
