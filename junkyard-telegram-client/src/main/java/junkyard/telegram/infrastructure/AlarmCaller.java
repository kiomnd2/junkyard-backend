package junkyard.telegram.infrastructure;

public interface AlarmCaller {
    boolean supports(AlarmType type);
    void sendMessage(String message);
}
