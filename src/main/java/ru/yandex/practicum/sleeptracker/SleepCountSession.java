package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SleepCountSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        if (sleepingSessions != null && !sleepingSessions.isEmpty()) {
            int count = sleepingSessions.size();
            return new SleepAnalysisResult("Количество сессий сна", count);
        } else {
            return new SleepAnalysisResult("Количество сессий сна", "нет данных");
        }

    }
}
