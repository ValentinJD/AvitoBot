package ru.avitobot.producer.http;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.avitobot.producer.dto.MessageDto;

@RestController
public class AvitoWebClient {

    private final WebClient avito;

    public AvitoWebClient(WebClient avito) {
        this.avito = avito;
    }

    public Mono<ResponseEntity<MessageDto>> getMessages(long userId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.avito.ru")
                .build();
        return webClient
                .get()
                .uri("/messenger/v2/accounts/" + userId+"/chats")
                .header("Authorization",
                        "Bearer d3tQAq5WTg2OB6ifzh1crgtBfEs-wM8HQkYArTDp")
                .header("Cookie", "srv_id=lsDmD9tnO20-BABU.6zdzpcsktYg8su8VQQ61lawI-Z1QT1wa43OKgjuRHvayVPWq4bS40UcTdMuCBVgAEnQb.VyNUZPYMDl8vTWIGIbGb0FwDCUmd1rSPNPoOrwOG3Kk=.web")
                .retrieve()
                .toEntity(MessageDto.class);
    }

    public Mono<ResponseEntity<MessageDto>> getTest(long userId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:8081")
                .build();
        return webClient
                .get()
                .uri("/test")
                .retrieve()
                .toEntity(MessageDto.class);
    }

//    @Scheduled(fixedDelay = 5000)
    public MessageDto start() {
        MessageDto s = getMessages(12542761)
                .block()
                .getBody();
        System.out.println(s);
        return s;
    }

}
