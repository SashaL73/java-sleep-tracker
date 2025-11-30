package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleepTrackerApp {
    private final List<Function<List<SleepingSession>, ?>> functionList = new ArrayList<>();

    public SleepTrackerApp() {
        functionList.add(new SleepCountSession());
        functionList.add(new MinSleepSession());
        functionList.add(new MaxSleepSession());
        functionList.add(new MiddleSleepSession());
        functionList.add(new BadSleeping());
        functionList.add(new NightSleepAnalyzer());
        functionList.add(new UserClassification());
    }

    public List<Function<List<SleepingSession>, ?>> getFunctionList() {
        return functionList;
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            SleepTrackerApp sleepTrackerApp = new SleepTrackerApp();
            String filePath = args[0];
            Path path = Paths.get(filePath);

            try (Stream<String> stream = Files.lines(path)) {
                long count = stream.count();
                if (count == 0) {
                    throw new IOException("Файл пуст.");
                }
            }

            try (Stream<String> stream = Files.lines(path)) {
                List<SleepingSession> sleepingSessions = stream
                        .map(line -> line.split(";"))
                        .map(lines -> new SleepingSession(lines[0], lines[1], lines[2]))
                        .collect(Collectors.toList());

                sleepTrackerApp.functionList.stream()
                        .forEach(function -> {
                            SleepAnalysisResult result = (SleepAnalysisResult) function.apply(sleepingSessions);
                            System.out.println(result.getDescription() + ": " + result.getResult());
                        });
            }
        } else {
            System.err.println("Путь к файлу не передан.");
        }
    }
}