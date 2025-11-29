package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        LocalDate startDate = sleepingSessions.getFirst().getStartSleep().toLocalDate();
        LocalDate endDate = sleepingSessions.getLast().getEndSleep().toLocalDate();

        Period period = Period.between(startDate, endDate);
        long nightCount = period.getDays();
        long sleeping = sessions.size();

        return new SleepAnalysisResult("количество бессонных ночей", nightCount - sleeping);
    }
}
