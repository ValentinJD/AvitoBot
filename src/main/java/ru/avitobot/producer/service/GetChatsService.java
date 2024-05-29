package ru.avitobot.producer.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.avitobot.producer.dto.Chat;
import ru.avitobot.producer.dto.MessageDto;
import ru.avitobot.producer.http.AvitoWebClient;

import java.util.List;

@Service
@AllArgsConstructor
public class GetChatsService {

    private final WebClient webClientAvito;
    private final AvitoWebClient avitoWebClient;

    public List<Chat> getMessages(Long userId) {
        return webClientAvito.get()
                .uri("/messenger/v2/accounts/" + userId + "/chats")
                .retrieve()
                .toEntity(MessageDto.class)
                .block()
                .getBody()
                .getChats();
    }

    public List<Chat> getChats() {
        return avitoWebClient.start().getChats();
    }

}
