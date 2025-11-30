package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class UserClassification implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        AtomicInteger counterOwl = new AtomicInteger();
        AtomicInteger counterLark = new AtomicInteger();
        AtomicInteger counterPigeon = new AtomicInteger();
        String userClassification = "";

        List<SleepingSession> sessions = sleepingSessions.stream()
                .filter(s -> s.endSleep.toLocalDate().equals(s.startSleep.toLocalDate().plusDays(1))
                        || s.startSleep.isBefore(LocalDateTime
                        .of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 6, 0))
                        && s.endSleep.toLocalDate().isBefore(LocalDate
                        .of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth() + 1)))
                .toList();

        List<SleepingSession> s1 = sessions.stream()
                .peek(s -> {
                    if (s.startSleep.toLocalTime().isAfter(LocalTime.of(23, 0))
                            && s.endSleep.toLocalTime().isAfter(LocalTime.of(9, 0))
                            || s.startSleep.toLocalTime().isBefore(LocalTime.of(6, 0))
                            && s.endSleep.toLocalTime().isAfter(LocalTime.of(9, 0))) {
                        counterOwl.getAndIncrement();
                    } else if (s.startSleep.isBefore(LocalDateTime
                            .of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 22, 0))
                            && s.endSleep.isBefore(LocalDateTime
                            .of(s.endSleep.getYear(), s.endSleep.getMonth(), s.endSleep.getDayOfMonth(), 7, 0))) {
                        counterLark.getAndIncrement();
                    } else {
                        counterPigeon.getAndIncrement();
                    }
                })
                .toList();

        if (counterPigeon.get() > counterLark.get() && counterPigeon.get() > counterOwl.get()) {
            userClassification = "Голубь";
        } else if (counterOwl.get() == counterLark.get()) {
            userClassification = "Голубь";
        } else if (counterLark.get() > counterOwl.get()) {
            userClassification = "Жаворонок";
        } else if (counterOwl.get() > counterLark.get()) {
            userClassification = "Сова";
        }

        return new SleepAnalysisResult("Вы", userClassification);
    }
}
