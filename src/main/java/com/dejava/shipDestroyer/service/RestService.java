package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.AuthRequest;
import com.dejava.shipDestroyer.model.AuthResponse;
import com.dejava.shipDestroyer.model.BattleshipPlacement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    @Value(value = "${rest.baseUrl}")
    private String baseUrl;

    @Value(value = "${rest.username}")
    private String authUser;

    @Value(value = "${rest.password}")
    private String authPass;

    public String authenticate() {
        String url = baseUrl + "/api/authenticate";

        AuthResponse response = new RestTemplate()
                .postForObject(url, new HttpEntity<>(new AuthRequest(authUser, authPass)), AuthResponse.class);

        return response != null ? response.getToken() : null;
    }

    public void registerToTournament(String tournamentId) {
        String url = baseUrl + "/api/tournaments/" + tournamentId + "/teams";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticate());

        new RestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<>("{}", headers), Void.class);
    }

    public void registerShipToTournament(String tournamentId, BattleshipPlacement placement) {
        String url = baseUrl + "/api/tournaments/" + tournamentId + "/battleships";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticate());

        try {
            String json = new ObjectMapper().writeValueAsString(placement);

            new RestTemplate().exchange(url, HttpMethod.POST, new HttpEntity<>(json, headers), Void.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
