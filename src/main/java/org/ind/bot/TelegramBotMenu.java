package org.ind.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramBotMenu {
    private ReplyKeyboard createMenuReplyKeyboard(List<InlineKeyboardButton> rowInlineKeyboardButtons) {

        InlineKeyboardMarkup keyboardMarkup
                = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(List.of(rowInlineKeyboardButtons));

        return keyboardMarkup;
    }

    public ReplyKeyboard getMenuLevelUserQualification() {
        return createMenuReplyKeyboard(List.of(getBtnKeyboard("First Post", "Show First Post"),
                getBtnKeyboard("Second Post", "Show Second Post")));
    }

    private InlineKeyboardButton getBtnKeyboard(String textBtn, String callbackData) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(textBtn);
        inlineKeyboardButton.setCallbackData(callbackData);
        return inlineKeyboardButton;
    }

    public ReplyKeyboard getBackToVacanciesMenu() {
        return createMenuReplyKeyboard(List.of(getBtnKeyboard("Back to vacancies", "Back to vacancies"),
                getBtnKeyboard("Generate With ChatGPT cover letter", "Generate With ChatGPT cover letter"),
                getBtnKeyboard("Back to start menu", "Back to start menu")));
    }
}