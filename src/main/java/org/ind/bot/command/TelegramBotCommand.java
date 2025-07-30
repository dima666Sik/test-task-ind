package org.ind.bot.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TelegramBotCommand {

    START("/start","Start general work of application."),
    HELP("/help","The help command, can show you some various resolves to often issues."),
    SEND_SIMPLE_POST("/send_simple_post","Send simple post to the channel.");

    private final String command;
    private final String description;
}