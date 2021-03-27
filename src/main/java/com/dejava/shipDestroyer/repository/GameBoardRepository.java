package com.dejava.shipDestroyer.repository;

import com.dejava.shipDestroyer.model.GameBoard;
import org.springframework.data.repository.CrudRepository;

public interface GameBoardRepository extends CrudRepository<GameBoard, String> {
}
