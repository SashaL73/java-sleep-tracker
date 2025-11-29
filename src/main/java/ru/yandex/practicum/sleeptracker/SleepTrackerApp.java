package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
        functionList.add(new Sleeping());
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

class SleepAnalysisResult {
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

class SleepCountSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> session) {
        int count = session.size();
        return new SleepAnalysisResult("Количество сессий сна", count);
    }
}

class MinSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        SleepingSession minSession = sleepingSessions.stream()
                .min(Comparator.comparing(s -> Duration.between(s.startSleep, s.endSleep)))
                .orElse(null);
        Duration minDuration = Duration.between(minSession.startSleep, minSession.endSleep);
        int min = Math.toIntExact(minDuration.toMinutes());


        return new SleepAnalysisResult("минимальная сессия в минутах", min);
    }
}

class MaxSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {

        SleepingSession maxSession = sleepingSessions.stream()
                .max(Comparator.comparing(s -> Duration.between(s.startSleep, s.endSleep)))
                .orElse(null);
        Duration maxDuration = Duration.between(maxSession.startSleep, maxSession.endSleep);
        int max = Math.toIntExact(maxDuration.toMinutes());
        return new SleepAnalysisResult("максимальная сессия в минутах", max);

    }
}

class MiddleSleepSession implements Function<List<SleepingSession>, SleepAnalysisResult> {

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

class BadSleeping implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        long count = (sleepingSessions.stream()
                .filter(s -> s.statusSleeping.equals("BAD"))
                .count());

        return new SleepAnalysisResult("количество плохих сессий", count);
    }
}

class Sleeping implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        List<SleepingSession> sessions = sleepingSessions.stream()
                .filter(s -> s.endSleep.toLocalDate().equals(s.startSleep.toLocalDate().plusDays(1))
                        || s.startSleep.isBefore(LocalDateTime.
                        of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 6, 0))
                        && s.endSleep.toLocalDate().isBefore(LocalDate.
                        of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth() + 1)))
                .toList();

        LocalDate startDate = sleepingSessions.getFirst().getStartSleep().toLocalDate();
        LocalDate endDate = sleepingSessions.getLast().getEndSleep().toLocalDate();

        Period period = Period.between(startDate, endDate);
        long nightCount = period.getDays();
        long sleeping = sessions.size();

        return new SleepAnalysisResult("количество бессонных ночей", nightCount - sleeping);
    }
}

class UserClassification implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        AtomicInteger count1 = new AtomicInteger();
        AtomicInteger count2 = new AtomicInteger();
        String userClassification = "";

        List<SleepingSession> sessions = sleepingSessions.stream()
                .filter(s -> s.endSleep.toLocalDate().equals(s.startSleep.toLocalDate().plusDays(1))
                        || s.startSleep.isBefore(LocalDateTime.
                        of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 6, 0))
                        && s.endSleep.toLocalDate().isBefore(LocalDate.
                        of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth() + 1)))
                .toList();

        List<SleepingSession> s1 = sessions.stream()
                .peek(s -> {
                    if (s.startSleep.toLocalTime().isAfter(LocalTime.of(23, 0))
                            && s.endSleep.toLocalTime().isAfter(LocalTime.of(9, 0))
                            || s.startSleep.toLocalTime().isBefore(LocalTime.of(6, 0))
                            && s.endSleep.toLocalTime().isAfter(LocalTime.of(9, 0))) {
                        count1.getAndIncrement();
                    } else if (s.startSleep.isBefore(LocalDateTime.
                            of(s.startSleep.getYear(), s.startSleep.getMonth(), s.startSleep.getDayOfMonth(), 22, 0))
                            && s.endSleep.isBefore(LocalDateTime.
                            of(s.endSleep.getYear(), s.endSleep.getMonth(), s.endSleep.getDayOfMonth(), 7, 0))) {
                        count2.getAndIncrement();
                    }
                })
                .toList();

        if (count1.get() == count2.get()) {
            userClassification = "Голубь";
        } else if (count1.get() > count2.get()) {
            userClassification = "Сова";
        } else if (count2.get() > count1.get()) {
            userClassification = "Жаворонок";
        }

        return new SleepAnalysisResult("Вы", userClassification);
    }
}