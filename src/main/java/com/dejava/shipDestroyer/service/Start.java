package com.dejava.shipDestroyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Start {

    @Autowired
    private RestService restService;

    @Value(value = "${tournamentId}")
    private String tournamentId;

    @PostConstruct
    public void init() {
        String token = restService.authenticate();
        restService.registerToTournament(token, tournamentId);
    }
}

