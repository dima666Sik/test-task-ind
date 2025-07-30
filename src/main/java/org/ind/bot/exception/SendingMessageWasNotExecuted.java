package org.ind.bot.exception;

public class SendingMessageWasNotExecuted extends RuntimeException{
    public SendingMessageWasNotExecuted() {
    }

    public SendingMessageWasNotExecuted(String message) {
        super(message);
    }

    public SendingMessageWasNotExecuted(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingMessageWasNotExecuted(Throwable cause) {
        super(cause);
    }
}