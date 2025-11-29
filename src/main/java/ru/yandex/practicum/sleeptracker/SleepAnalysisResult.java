package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    private String description;
    private Object result;

    public SleepAnalysisResult(String description, Object result) {
        this.description = description;
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public Object getResult() {
        return result;
    }
}
