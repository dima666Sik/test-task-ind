package org.ind.bot;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ind.bot.config.TelegramBotConfig;
import org.ind.bot.exception.SendingMessageWasNotExecuted;
import org.ind.bot.exception.SettingCommandWasNotExecuted;
import org.ind.bot.service.PostService;
import org.ind.bot.service.PostServiceImpl;
import org.ind.bot.util.BotCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.ind.bot.command.TelegramBotCommand.HELP;
import static org.ind.bot.command.TelegramBotCommand.START;

@Component
@Log4j2
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final TelegramBotMenu telegramBotMenu;
    private final PostService postService;
    private final TelegramBotMenuCommand telegramBotMenuCommand;
    private final TelegramBotConfig telegramBotConfig;
    private SendMessage sendMessage;

    // key: chat id, value: lvl
    private final Map<Long, String> mapLastShowLvl = new HashMap<>();
    private static final Long FIRST_POST_ID = 1L;
    private static final Long SECOND_POST_ID = 2L;


    @PostConstruct
    public void init() {
        try {
            log.info("start execute set commands!");
            this.execute(new SetMyCommands(telegramBotMenuCommand.getBotCommandList(),
                new BotCommandScopeDefault(), null));
            log.info("successful execute set commands!");
        } catch (TelegramApiException e) {
            log.error("execute set commands was wrong!");
            throw new SettingCommandWasNotExecuted("Execute set commands was wrong!", e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage()) {
            if (update.getMessage().getText().equals(START.getCommand())) {
                handleMessageReceived(update);
            } else if (update.getMessage().getText().equals(HELP.getCommand())) {
                handleCallHelp(update);
            }
        }
        if (update.hasCallbackQuery()) {
            handleCallQuery(update);
        }
    }

    private void handleCallHelp(Update update) {
        basicSettingSendMessage(update.getMessage().getChatId(),
            BotCommand.HELP_TEXT);
        sendMessage(Optional.empty());
    }

    private void handleCallQuery(Update update) {
        String callBackData = update.getCallbackQuery().getData();
        if (callBackData.equals("Show First Post")) {
            showFirstPostUpdate(update);
        } else if (callBackData.equals("Show Second Post")) {
            showSecondPostUpdate(update);
        } else if (callBackData.equals("Back to start menu")) {
            handleBackToStartMenu(update);
        }
    }

    private void handleBackToStartMenu(Update update) {
        basicSettingSendMessage(update.getCallbackQuery().getMessage().getChatId(),
            "Please choose your level:");
        sendMessage(Optional.of(telegramBotMenu.getMenuLevelUserQualification()));
    }

    private void showPost(Long id, Update update) {
        var post = postService.getPostById(id);

        String title = post.getTitle();
        String summary = post.getSummary();
        String content = post.getContent();
        String author = post.getAuthor();
        String tags = post.getTags();
        String sourceUrl = post.getSourceUrl();

        Long chatId = update.getCallbackQuery().getMessage().getChatId();

        String postText = """
            *Title:* %s \n\n
            *Summary:* %s \n\n
            *Content:* %s \n\n
            *Author:* %s \n\n
            *Tags:* %s \n\n
            *Source:* [%s](%s)
            """.formatted(
            escapeReservedMarkdownCharacters(title),
            escapeReservedMarkdownCharacters(summary),
            escapeReservedMarkdownCharacters(content),
            escapeReservedMarkdownCharacters(author),
            escapeReservedMarkdownCharacters(tags),
            escapeReservedMarkdownCharacters("Click to read more"),
            escapeReservedMarkdownCharacters(sourceUrl));

        basicSettingSendMessage(chatId, postText);
        sendMessage.setParseMode(ParseMode.MARKDOWNV2);
        sendMessage(Optional.of(telegramBotMenu.getBackToVacanciesMenu()));
    }


    private String escapeReservedMarkdownCharacters(String str) {
        return str.replace("*", "\\*")
            .replace("_", "\\_")
            .replace("-", "\\-")
            .replace("[", "\\[")
            .replace("]", "\\]")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("~", "\\~")
            .replace("`", "\\`")
            .replace(">", "\\>")
            .replace("#", "\\#")
            .replace("+", "\\+")
            .replace(".", "\\.")
            .replace("!", "\\!");

    }

    private void showFirstPostUpdate(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        basicSettingSendMessage(chatId,
            "This is the first post:");
        showPost(FIRST_POST_ID, update);
        mapLastShowLvl.put(chatId, "First Post");
    }

    private void showSecondPostUpdate(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        basicSettingSendMessage(chatId,
            "This is the second post:");
        showPost(SECOND_POST_ID, update);
        mapLastShowLvl.put(chatId, "Second Post");
    }

    private void handleMessageReceived(Update update) {
        basicSettingSendMessage(update.getMessage().getChatId(),
            "Welcome to the " + telegramBotConfig.getUsername() + "! Please choose the post:");
        sendMessage(
            Optional.of(telegramBotMenu.getMenuLevelUserQualification()));
    }

    private void basicSettingSendMessage(Long id, String textMessage) {
        sendMessage = new SendMessage();
        sendMessage.setChatId(id);
        sendMessage.setText(textMessage);
    }

    private void sendMessage(Optional<ReplyKeyboard> replyKeyboard) {

        replyKeyboard.ifPresent(sendMessage::setReplyMarkup);

        try {
            log.info("start execute send message!");
            execute(sendMessage);
            log.info("successful execute send message!");
        } catch (TelegramApiException e) {
            log.error("execute send message was wrong!");
            throw new SendingMessageWasNotExecuted("The sending message was not executed!", e);
        }
    }

    @Override
    public String getBotUsername() {
        return telegramBotConfig.getUsername();
    }

    @Override
    public String getBotToken() {
        return telegramBotConfig.getAccessToken();
    }
}