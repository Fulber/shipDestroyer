package com.dejava.shipDestroyer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.annotation.PostConstruct;

@Component
public class Start {

    private final Logger logger = LoggerFactory.getLogger(Start.class);

    @Autowired
    private RestService restService;

    @Value(value = "${tournamentId}")
    private String tournamentId;

    @PostConstruct
    public void init() {
        while (true) {
            try {
                restService.registerToTournament(tournamentId);

                logger.info("-------------- REGISTERED TO TOURNAMENT");
                break;
            } catch (HttpServerErrorException hse) {
                logger.error("-------------- SERVER DOWN");
                break;
            } catch (HttpClientErrorException hce) {
                logger.error("-------------- ALREADY REGISTERED TO TOURNAMENT");
                break;
            }
        }
    }
}

