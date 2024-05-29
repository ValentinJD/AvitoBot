package ru.avitobot.producer.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.avitobot.producer.bot.Utils;
import ru.avitobot.producer.dto.MessageDto;
import ru.avitobot.producer.http.AvitoWebClient;

import java.util.List;

@Slf4j
@Component
public class GetChatsCommand extends ServiceCommand {

    private final AvitoWebClient avitoWebClient;

    public GetChatsCommand(String identifier, String description, AvitoWebClient avitoWebClient) {
        super(identifier, description);
        this.avitoWebClient = avitoWebClient;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        String textMessage = "Имеются следующие чаты: %s";
        List<ru.avitobot.producer.dto.Chat> chats1 = avitoWebClient.start().getChats();
        ru.avitobot.producer.dto.Chat chat1 = chats1.get(0);
        String chats =
              "Id =" + chat1.getId() +
                     "Title" +  chat1.getContext().getValue().getTitle() +
                 "Price" +       chat1.getContext().getValue().getPrice_string()
                ;
        String message = String.format(textMessage, chats).replace("&nbsp;", " ");

        sendMessageLogged(absSender, chat, userName, message);
    }

    private void sendMessageLogged(AbsSender absSender, Chat chat, String userName, String textMessage) {
        log.debug(String.format("Пользователь %s. Начато выполнение команды %s", userName,
                this.getCommandIdentifier()));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, textMessage);
        log.debug(String.format("Пользователь %s. Завершено выполнение команды %s", userName,
                this.getCommandIdentifier()));
    }
}
