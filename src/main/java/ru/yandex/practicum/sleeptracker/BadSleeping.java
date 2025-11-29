package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class BadSleeping implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long count = (sleepingSessions.stream()
                .filter(s -> s.statusSleeping.equals("BAD"))
                .count());

        return new SleepAnalysisResult("количество плохих сессий", count);
    }
}
