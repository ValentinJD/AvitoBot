package ru.avitobot.producer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFlux
@EnableWebFluxSecurity
public class WebClientConfig {

    @Bean
    public ReactiveClientRegistrationRepository getRegistration(
            @Value("${spring.security.oauth2.client.provider.avito.token-uri}") String token_uri,
            @Value("${spring.security.oauth2.client.registration.avito.client-id}") String client_id,
            @Value("${spring.security.oauth2.client.registration.avito.client-secret}") String client_secret) {
        ClientRegistration registration = ClientRegistration
                .withRegistrationId("avito")
                .tokenUri(token_uri)
                .clientId(client_id)
                .clientSecret(client_secret)
                .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
                .build();
        return new InMemoryReactiveClientRegistrationRepository(registration);
    }

    @Bean(name = "avito")
    public WebClient webClientAvito(ReactiveClientRegistrationRepository clientRegistrations) {
        var clientService = new InMemoryReactiveOAuth2AuthorizedClientService(clientRegistrations);
        var clientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrations, clientService);
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientManager);
        oauth.setDefaultClientRegistrationId("avito");
        return WebClient.builder()
                .filter(oauth)
                .baseUrl("https://api.avito.ru")
                .build();
    }

//    @Bean
//    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrationRepo,
//                               ServerOAuth2AuthorizedClientRepository authorizedClientRepo) {
//        ServerOAuth2AuthorizedClientExchangeFilterFunction filter =
//                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepo, authorizedClientRepo);
//
//        return WebClient.builder().filter(filter).build();
//    }
}
