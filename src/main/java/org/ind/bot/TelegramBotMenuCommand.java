package org.ind.bot;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

import static org.ind.bot.command.TelegramBotCommand.HELP;
import static org.ind.bot.command.TelegramBotCommand.START;


@Component
@Getter
public class TelegramBotMenuCommand {
    private final List<BotCommand> botCommandList = new ArrayList<>();

    @PostConstruct
    public void init(){
        botCommandList.add(new BotCommand(START.getCommand(), START.getDescription()));
        botCommandList.add(new BotCommand(HELP.getCommand(), HELP.getDescription()));
    }
}