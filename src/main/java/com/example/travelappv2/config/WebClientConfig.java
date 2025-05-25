package com.example.travelappv2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Configuration pour les clients HTTP utilisés dans l'application
 */
@Configuration
public class WebClientConfig {
    
    /**
     * Configure un RestTemplate avec un timeout plus long pour les appels API externes
     */
    @Bean(name = "groqRestTemplate")
    public RestTemplate groqRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
        return restTemplate;
    }
    
    /**
     * Configure une factory pour les requêtes HTTP avec des timeouts adaptés
     */
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000); // 15 secondes de timeout de connexion
        factory.setReadTimeout(20000);    // 20 secondes de timeout de lecture
        return factory;
    }
}
