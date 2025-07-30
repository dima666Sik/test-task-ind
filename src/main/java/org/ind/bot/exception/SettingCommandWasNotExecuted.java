package org.ind.bot.exception;

public class SettingCommandWasNotExecuted extends RuntimeException{
    public SettingCommandWasNotExecuted() {
    }

    public SettingCommandWasNotExecuted(String message) {
        super(message);
    }

    public SettingCommandWasNotExecuted(String message, Throwable cause) {
        super(message, cause);
    }

    public SettingCommandWasNotExecuted(Throwable cause) {
        super(cause);
    }
}