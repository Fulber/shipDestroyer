package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.BattleshipCell;
import com.dejava.shipDestroyer.model.GameStartedEvent;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessingService {
  private boolean checkCanvas() {
    return true;
  }

  private HashMap<String, Integer> possibleOptions = new HashMap<String, Integer>();

  public BattleshipCell[][] rotateClockWise(BattleshipCell[][] canvas) {
    int n = canvas.length;
    int m = canvas[0].length;
    BattleshipCell [][] output = new BattleshipCell [m][n];

    for (int i = 0; i < n; i++)
      for (int j = 0; j < m; j++)
        output [j][n - 1 - i] = canvas[i][j];
    return output;
  }

  private void addOptions(BattleshipCell[][] canvas, int boardY, int boardX, int battlegroundSize, String[][] battleground) {
    int height = canvas.length;
    int width = canvas[0].length;

    for (int templateY = 0; templateY < height; templateY++) {
      for (int templateX = 0; templateX < width; templateX++) {
        if (canvas[templateY][templateX].getHp() != 0) {
          boolean ok = true;
          for (int canvasY = 0; canvasY < height; canvasY++) {
            if (!ok) break;
            for (int canvasX = 0; canvasX < width; canvasX++) {
              int newY = boardY - templateY + canvasY;
              int newX = boardX - templateX + canvasX;
              if (newY < 0 || newY > battlegroundSize) { ok = false; break; }
              if (newX < 0 || newX > battlegroundSize) { ok = false; break; }
              if (battleground[newY][newX] == "HIT" && canvas[canvasY][canvasX].getHp() == 0) { ok = false; break; }
              if (battleground[newY][newX] == "MISS" && canvas[canvasY][canvasX].getHp() != 0) { ok = false; break; }
            }
          }
          if (ok) {
            for (int canvasY = 0; canvasY < height; canvasY++) {
              for (int canvasX = 0; canvasX < width; canvasX++) {
                if (canvas[canvasY][canvasX].getHp() != 0) {
                  int newY = boardY - templateY + canvasY;
                  int newX = boardX - templateX + canvasX;
                  String key = newY + "-" + newX;
                  if (possibleOptions.containsKey(key)) {
                    possibleOptions.put(key, possibleOptions.get(key) + 1);
                  } else {
                    possibleOptions.put(key, 1);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  public void onHitEvent(int boardX, int boardY) {
    GameStartedEvent game = new GameStartedEvent();
    int battlegroundSize = game.getBattlegroundSize();

    String[][] battleground = new String[battlegroundSize][battlegroundSize];

    var battleshipTemplate = game.getBattleshipTemplate();
    var canvasNorth = battleshipTemplate.getCanvas();
    this.addOptions(canvasNorth, boardY,boardX,battlegroundSize,battleground);
    var canvasEast = rotateClockWise(canvasNorth);
    this.addOptions(canvasEast, boardY,boardX,battlegroundSize,battleground);
    var canvasSouth = rotateClockWise(canvasEast);
    this.addOptions(canvasSouth, boardY,boardX,battlegroundSize,battleground);
    var canvasWest = rotateClockWise(canvasSouth);
    this.addOptions(canvasWest, boardY,boardX,battlegroundSize,battleground);

     String maxKey = Collections.max(possibleOptions.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
     String[] arrOfStr =maxKey.split("-");
     int y = Integer.valueOf(arrOfStr[0]);
     int x = Integer.valueOf(arrOfStr[1]);
  }
}
