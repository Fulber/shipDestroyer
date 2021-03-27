package com.dejava.shipDestroyer.repository;

import com.dejava.shipDestroyer.data.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Token findById(long id);
}