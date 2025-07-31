package org.ind.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.ind.bot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@Getter
@Setter
public class TelegramBotConfig {
    @Value("${telegram.bot.access.token}")
    private String accessToken;
    @Value("${telegram.bot.username}")
    private String username;
}