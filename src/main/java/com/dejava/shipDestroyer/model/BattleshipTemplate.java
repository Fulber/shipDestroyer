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
    private BattleshipCell[][] canvas;

    @Lob
    @Column
    private String canvasJson;

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

    public BattleshipCell[][] getCanvas() {
        try {
            this.canvas = new ObjectMapper().readValue(canvasJson, BattleshipCell[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return canvas;
    }

    public void setCanvas(BattleshipCell[][] canvas) {
        this.canvas = canvas;

        try {
            this.canvasJson = new ObjectMapper().writeValueAsString(canvas);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String getCanvasJson() {
        return canvasJson;
    }

    public void setCanvasJson(String canvasJson) {
        this.canvasJson = canvasJson;

        try {
            this.canvas = new ObjectMapper().readValue(canvasJson, BattleshipCell[][].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
