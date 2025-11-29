package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    final LocalDateTime startSleep;
    final LocalDateTime endSleep;
    final String statusSleeping;

    public SleepingSession(String startSleep, String endSleep, String statusSleeping) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        this.startSleep = LocalDateTime.parse(startSleep, formatter);
        this.endSleep = LocalDateTime.parse(endSleep, formatter);
        this.statusSleeping = statusSleeping;

    }

    public LocalDateTime getStartSleep() {
        return startSleep;
    }

    public LocalDateTime getEndSleep() {
        return endSleep;
    }

}
