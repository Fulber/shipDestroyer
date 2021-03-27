package com.dejava.shipDestroyer.repository;

import com.dejava.shipDestroyer.model.GameStartedEvent;
import org.springframework.data.repository.CrudRepository;

public interface GameStartedEventRepository extends CrudRepository<GameStartedEvent, String> {
}
