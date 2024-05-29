package ru.avitobot.producer.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.avitobot.producer.bot.commands.GetChatsCommand;
import ru.avitobot.producer.bot.commands.StartCommand;
import ru.avitobot.producer.http.AvitoWebClient;

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
    public GetChatsCommand getChatsCommand(AvitoWebClient avitoWebClient) {
        return new GetChatsCommand("allChats", "Все чаты", avitoWebClient);
    }

}
