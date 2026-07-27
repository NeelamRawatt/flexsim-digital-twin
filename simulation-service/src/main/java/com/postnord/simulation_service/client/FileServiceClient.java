package com.postnord.simulation_service.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.postnord.simulation_service.exception.FileServiceUnavailableException;

@Component
public class FileServiceClient {

    private final RestClient restClient;

    public FileServiceClient(@Value("${services.file.base-url}") String fileBaseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);

        this.restClient = RestClient.builder()
                .baseUrl(fileBaseUrl)
                .requestFactory(factory)
                .build();
    }

    public boolean filesReady(Long experimentId) {
        try {
            Boolean ready = restClient.get()
                    .uri("/api/files/exists/{simExpId}", experimentId)
                    .retrieve()
                    .body(Boolean.class);
            return Boolean.TRUE.equals(ready);
        } catch (Exception e) {
            // Fail safe: if we can't confirm the files exist, don't let the simulation start.
            throw new FileServiceUnavailableException(
                    "Could not verify uploaded files — File Service may be unreachable", e);
        }
    }
}