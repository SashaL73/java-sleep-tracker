package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class SleepCountSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> session) {
        int count = session.size();
        return new SleepAnalysisResult("Количество сессий сна", count);
    }
}
