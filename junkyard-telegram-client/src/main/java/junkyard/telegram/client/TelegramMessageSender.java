package junkyard.telegram.client;

public interface TelegramMessageSender {
    void sendMessage(String message, Object ... args);
}
