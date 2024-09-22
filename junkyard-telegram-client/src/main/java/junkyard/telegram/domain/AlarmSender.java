package junkyard.telegram.domain;

public interface AlarmSender {
    void sendMessage(String method, AlarmCommand.RequestAlarm alarm);
}
