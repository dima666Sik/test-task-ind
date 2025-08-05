package org.ind.telegram.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TelegramService {

    public void handleNewPost(String chatTitle, String text) {
        log.info("New post from {}: {}", chatTitle, text);
    }
}
