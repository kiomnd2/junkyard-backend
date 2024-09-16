package junkyard.telegram.domain;

public interface AlarmSender {
    void sendMessage(AlarmCommand.RequestAlarm alarm);
}
