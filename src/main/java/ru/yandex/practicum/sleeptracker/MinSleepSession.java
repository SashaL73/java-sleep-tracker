package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class MinSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        if (sleepingSessions != null && !sleepingSessions.isEmpty()) {

            SleepingSession minSession = sleepingSessions.stream()
                    .min(Comparator.comparing(s -> Duration.between(s.startSleep, s.endSleep)))
                    .orElse(null);
            Duration minDuration = Duration.between(minSession.startSleep, minSession.endSleep);
            int min = Math.toIntExact(minDuration.toMinutes());

            return new SleepAnalysisResult("минимальная сессия в минутах", min);
        } else {
            return new SleepAnalysisResult("минимальная сессия в минутах", "нет данных");
        }

    }
}
