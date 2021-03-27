package com.dejava.shipDestroyer;

import com.dejava.shipDestroyer.service.RestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class ShipDestroyerApplicationTests {

    @Autowired
    private RestService restService;

    @Test
    void contextLoads() {
    }

    @Test
    void authenticationWorks() {
        String token = restService.authenticate();

        Assert.notNull(token, "kek");
    }
}
