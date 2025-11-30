package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MiddleSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        final long[] i = {0};
        List<Duration> durations = sleepingSessions.stream()
                .map(s -> Duration.between(s.startSleep, s.endSleep))
                .peek(duration -> i[0] = i[0] + Math.toIntExact(duration.toMinutes()))
                .toList();
        long middle = i[0] / durations.size();

        return new SleepAnalysisResult("средняя продолжительность сессии в минутах", middle);
    }
}
