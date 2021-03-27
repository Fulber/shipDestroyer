package com.dejava.shipDestroyer;

import com.dejava.shipDestroyer.data.Token;
import com.dejava.shipDestroyer.model.BattleshipPlacement;
import com.dejava.shipDestroyer.repository.TokenRepository;
import com.dejava.shipDestroyer.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Random;

@SpringBootTest
class ShipDestroyerApplicationTests {

    @Autowired
    private RestService restService;

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void authenticationWorks() {
        String token = restService.authenticate();
        Assert.notNull(token, "notNull");
    }

    @Test
    void registerWorks() {
        String token = restService.authenticate();

        restService.registerToTournament(token, "Wd6MzuHU16");
    }

    @Test
    void dbWorks() {
        tokenRepository.save(new Token(100L, "test"));
    }

    @Test
    void testJson() {
        Random r = new Random();

        int x = r.nextInt(14);
        int y = r.nextInt(14);

        BattleshipPlacement placement = new BattleshipPlacement("asd", x, y, BattleshipPlacement.DIRECTION.EAST);

        try {
            String json = new ObjectMapper().writeValueAsString(placement);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
