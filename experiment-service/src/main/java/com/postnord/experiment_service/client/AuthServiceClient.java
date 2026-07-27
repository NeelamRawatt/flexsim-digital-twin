package com.postnord.experiment_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.postnord.experiment_service.exception.AuthServiceUnavailableException;

                                                                                                                                               

@Component
public class AuthServiceClient {
    
    private final RestClient restClient;

    

    public AuthServiceClient(@Value("${services.auth.base-url}") String authBaseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // give up trying to connect after 3 seconds
        factory.setReadTimeout(5000);    // give up waiting for a reply after 5 seconds
        

        this.restClient=RestClient.builder()
                    .baseUrl(authBaseUrl)
                    .requestFactory(factory)
                    .build();
    
    }



    public boolean userExists(String username)
    {
        try{
            Boolean exists = restClient.get()
                        .uri("/api/auth/exists/{username}",username)
                        .retrieve()
                        .body(Boolean.class);
            return Boolean.TRUE.equals(exists);
        }
        catch(Exception e)
        {
            throw new AuthServiceUnavailableException("Could not verify username - Auth Service may be unreachable ",e);
        }
    }

   
}
