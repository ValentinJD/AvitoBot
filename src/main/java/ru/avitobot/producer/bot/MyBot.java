package ru.avitobot.producer.bot;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.avitobot.producer.bot.commands.GetChatsCommand;
import ru.avitobot.producer.bot.commands.StartCommand;
import ru.avitobot.producer.dto.MessageDto;
import ru.avitobot.producer.http.AvitoWebClient;

import java.util.HashMap;
import java.util.Map;

@Service()
public class MyBot extends TelegramLongPollingCommandBot {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * Настройки файла для разных пользователей. Ключ - уникальный id чата
     */
    @Getter
    private static Map<Long, String> userSettings;
    @Autowired
    private AvitoWebClient avitoWebClient;
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    public MyBot(@Value("${bot.name}") String botName,
                 @Value("${bot.token}") String botToken,
                 StartCommand botCommand,
                 GetChatsCommand getChatsCommand) {
        super();
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;

        register(botCommand);
        logger.debug("Команда start создана");

        register(getChatsCommand);
        logger.debug("Команда all_chats создана");

        register(new HelpCommand());
        logger.debug("Команда help создана");

        userSettings = new HashMap<>();
        logger.info("Бот создан!");
    }

    @Override
    public String getBotUsername() {
        return this.BOT_NAME;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        MessageDto start = avitoWebClient.start();
        Long chatId = msg.getChatId();
        String userName = Utils.getUserName(msg);

        String answer = nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    private String nonCommandExecute(Long chatId, String userName, String text) {
        return String.format("test msg chatId %s userName %s text %s",
                chatId, userName, text);
    }

    @Override
    public String getBotToken() {
        return this.BOT_TOKEN;
    }

    /**
     * Отправка ответа
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error(String.format("Ошибка %s. Сообщение, не являющееся командой. Пользователь: %s", e.getMessage(),
                    userName));
            e.printStackTrace();
        }
    }
}
