package org.ind.bot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TelegramBotConfig {
    @Value("${telegram.bot.access.token}")
    private String accessToken;
    @Value("${telegram.bot.username}")
    private String username;
}