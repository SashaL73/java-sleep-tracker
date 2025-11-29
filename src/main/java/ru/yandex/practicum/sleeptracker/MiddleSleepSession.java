package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MiddleSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        AtomicInteger d = new AtomicInteger();
        List<Duration> durations = sleepingSessions.stream()
                .map(s -> Duration.between(s.startSleep, s.endSleep))
                .peek(duration -> d.set(Math.toIntExact(duration.toMinutes())))
                .toList();
        long middle = d.get() / durations.size();

        return new SleepAnalysisResult("средняя продолжительность сессии в минутах", middle);
    }
}
