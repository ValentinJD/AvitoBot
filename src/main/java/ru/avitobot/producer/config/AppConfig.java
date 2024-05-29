package ru.avitobot.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.avitobot.producer.bot.commands.GetChatsCommand;
import ru.avitobot.producer.bot.commands.StartCommand;
import ru.avitobot.producer.http.AvitoWebClient;
import ru.avitobot.producer.service.GetChatsService;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public StartCommand getBotCommand() {
        return new StartCommand("start", "Старт");
    }

    @Bean
    public GetChatsCommand getChatsCommand(AvitoWebClient avitoWebClient,
                                           WebClient webClientAvito,
                                           GetChatsService getChatsService) {
        return new GetChatsCommand("allChats", "Все чаты",
        getChatsService);
    }

}
