package org.ind.telegram.service;

import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostHandler {
    @Value("${telegram.bot.username}")
    private String telegramBotUsername;
    private final HugoService hugoService;

    public void onMessage(TdApi.UpdateNewMessage update, SimpleTelegramClient client) {
        long chatId = update.message.chatId;
        TdApi.MessageContent content = update.message.content;

        if (!(content instanceof TdApi.MessageText msgText)) return;

        client.send(new TdApi.GetChat(chatId)).whenCompleteAsync((chat, ex) -> {
            if (ex != null) return;

            hugoService.handleNewPost(telegramBotUsername, msgText.text.text);
        });
    }
}
