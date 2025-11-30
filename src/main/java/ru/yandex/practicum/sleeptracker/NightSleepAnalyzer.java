package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;

public class NightSleepAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        List<SleepingSession> sessions = sleepingSessions.stream()
                .filter(s -> s.endSleep.toLocalDate().equals(s.startSleep.toLocalDate().plusDays(1))
                        || s.startSleep.isBefore(LocalDateTime
                        .of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 6, 0))
                        && s.endSleep.toLocalDate().isBefore(LocalDate
                        .of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth() + 1)))
                .toList();

        LocalDateTime startDate = sleepingSessions.getFirst().getStartSleep();
        LocalDateTime endDate = sleepingSessions.getLast().getEndSleep();
        long nightCount = 0;
        long counterSleepless = 0;

        if (sleepingSessions.getFirst().startSleep.toLocalTime().isAfter(LocalTime.of(12, 0))) {
            Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
            nightCount = period.getDays();
        } else if (sleepingSessions.getFirst().startSleep.toLocalTime().isBefore(LocalTime.of(12, 0))) {
            Period period = Period.between(startDate.toLocalDate().minusDays(1), endDate.toLocalDate());
            nightCount = period.getDays();
        }

        if (nightCount >= 1 && (!sessions.isEmpty())) {
            long sleeping = sessions.size();
            counterSleepless = nightCount - sleeping;
        } else {
            counterSleepless = nightCount;
        }

        return new SleepAnalysisResult("количество бессонных ночей", counterSleepless);
    }
}
