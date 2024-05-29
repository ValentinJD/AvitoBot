package ru.avitobot.producer.bot.commands;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.avitobot.producer.bot.Utils;
import ru.avitobot.producer.service.GetChatsService;


@Slf4j
@Component
public class GetChatsCommand extends ServiceCommand {

    private final GetChatsService getChatsService;

    public GetChatsCommand(String identifier, String description, GetChatsService getChatsService) {
        super(identifier, description);
        this.getChatsService = getChatsService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        String textMessage = "Имеются следующие чаты: %s";
        ru.avitobot.producer.dto.Chat chat1 = getChatsService.getChats().get(0);
        String chats =
                "Id =" + chat1.getId() +
                        "Title" + chat1.getContext().getValue().getTitle() +
                        "Price" + chat1.getContext().getValue().getPrice_string();
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
