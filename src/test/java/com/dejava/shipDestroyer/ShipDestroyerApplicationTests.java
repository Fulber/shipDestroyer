package com.dejava.shipDestroyer;

import com.dejava.shipDestroyer.data.Token;
import com.dejava.shipDestroyer.repository.TokenRepository;
import com.dejava.shipDestroyer.service.RestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

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
}
