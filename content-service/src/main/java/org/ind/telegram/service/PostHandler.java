package org.ind.telegram.service;

import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.jni.TdApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class PostHandler {

    private final TelegramService telegramService;
    private final SimpleTelegramClient client;

    public void onMessage(TdApi.UpdateNewMessage update) {
        long chatId = update.message.chatId;
        TdApi.MessageContent content = update.message.content;

        if (!(content instanceof TdApi.MessageText msgText)) return;

        client.send(new TdApi.GetChat(chatId)).whenCompleteAsync((chat, ex) -> {
            if (ex != null) return;

            String chatTitle = chat.title;
            if ("MyChannelName".equals(chatTitle)) {
                telegramService.handleNewPost(chatTitle, msgText.text.text);
            }
        });
    }
}
