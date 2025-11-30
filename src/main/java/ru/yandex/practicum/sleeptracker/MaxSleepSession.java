package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MaxSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        if (sleepingSessions != null && !sleepingSessions.isEmpty()) {
            SleepingSession maxSession = sleepingSessions.stream()
                    .max(Comparator.comparing(s -> Duration.between(s.startSleep, s.endSleep)))
                    .orElse(null);
            Duration maxDuration = Duration.between(maxSession.startSleep, maxSession.endSleep);
            int max = Math.toIntExact(maxDuration.toMinutes());
            return new SleepAnalysisResult("максимальная сессия в минутах", max);
        } else {
            return new SleepAnalysisResult("максимальная сессия в минутах", "нет данных");
        }

    }
}
