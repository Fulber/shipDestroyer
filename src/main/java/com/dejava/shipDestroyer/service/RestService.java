package com.dejava.shipDestroyer.service;

import com.dejava.shipDestroyer.model.AuthRequest;
import com.dejava.shipDestroyer.model.AuthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private final String authUrl = "https://battleships.cc/api/authenticate";
    private final String registerUrl = "api/tournaments/";
    private final String registerShipUrl = "/tournaments/{tournamentId}/battleships";

    @Value(value = "${rest.username}")
    private String authUser;

    @Value(value = "${rest.password}")
    private String authPass;

    public String authenticate() {
        HttpEntity<AuthRequest> request = new HttpEntity<>(new AuthRequest(authUser, authPass));
        AuthResponse response = new RestTemplate().postForObject(authUrl, request, AuthResponse.class);

        return response != null ? response.getToken() : null;
    }

    public void registerToTournament(String tournamentId) {
        String url = registerUrl + tournamentId + "/teams";
        new RestTemplate().postForObject(url, new HttpEntity<String>(""), Void.class);
    }
}
