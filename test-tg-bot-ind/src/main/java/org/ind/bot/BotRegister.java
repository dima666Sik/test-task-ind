package org.ind.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
@RequiredArgsConstructor
public class BotRegister {
    private final TelegramBot telegramBot;

    @PostConstruct
    @SneakyThrows
    public void init(){
        TelegramBotsApi telegramBotsApi =
                new TelegramBotsApi(DefaultBotSession.class);

        telegramBotsApi.registerBot(telegramBot);
    }
}
