package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.GameStartedEvent;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessingService {
  public void onHitEvent(int boardX, int boardY) {
    GameStartedEvent game = new GameStartedEvent();
    int battlegroundSize = game.getBattlegroundSize();

    String[][] battleground = new String[battlegroundSize][battlegroundSize];
    HashMap<String, Integer> possibleOptions = new HashMap<String, Integer>();

    battleground[4][6] = "hit";
    battleground[5][5] = "miss";

    var battleshipTemplate = game.getBattleshipTemplate();
    var canvas = battleshipTemplate.getCanvasList();
    int width = battleshipTemplate.getWidth();
    int height = battleshipTemplate.getHeight();

    for (int templateY = 0; templateY < height; templateY++) {
      for (int templateX = 0; templateX < width; templateX++) {
        if (canvas[templateY][templateX].getHp() != 0) {
          boolean ok = true;
          for (int canvasY = 0; canvasY < height; canvasY++) {
            if (!ok) break;
            for (int canvasX = 0; canvasX < width; canvasX++) {
              if (!ok) break;
              if (boardY - templateY + canvasY < 0 || boardY - templateY + canvasY > battlegroundSize) { ok = false; break; }
              if (boardX - templateX + canvasX < 0 || boardX - templateX + canvasX > battlegroundSize) { ok = false; break; }
              if (battleground[boardY - templateY + canvasY][boardX - templateX + canvasX] == "hit" && canvas[canvasY][canvasX].getHp() == 0) { ok = false; break; }
              if (battleground[boardY - templateY + canvasY][boardX - templateX + canvasX] == "miss" && canvas[canvasY][canvasX].getHp() != 0) { ok = false; break; }
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

     String maxKey = Collections.max(possibleOptions.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
     String[] arrOfStr =maxKey.split("-");
     int y = Integer.valueOf(arrOfStr[0]);
     int x = Integer.valueOf(arrOfStr[1]);
  }
}
