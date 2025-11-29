package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SleepTrackerAppTest {

    List<SleepingSession> sessions = Arrays.asList(
            new SleepingSession("01.10.25 21:50", "02.10.25 05:30", "BAD")
            , new SleepingSession("02.10.25 12:00", "02.10.25 14:10", "GOOD")
            , new SleepingSession("02.10.25 22:50", "03.10.25 05:20", "NORMAL")
            , new SleepingSession("04.10.25 00:50", "04.10.25 10:30", "BAD")
            , new SleepingSession("04.10.25 21:50", "05.10.25 04:30", "BAD")
            , new SleepingSession("05.10.25 21:50", "06.10.25 09:30", "BAD")
            , new SleepingSession("06.10.25 12:50", "06.10.25 15:30", "BAD")
            , new SleepingSession("06.10.25 23:50", "07.10.25 07:30", "GOOD")
            , new SleepingSession("08.10.25 00:30", "08.10.25 11:30", "NORMAL")
            , new SleepingSession("08.10.25 21:50", "09.10.25 05:30", "NORMAL")
            , new SleepingSession("10.10.25 07:30", "10.10.25 15:30", "NORMAL"));


    SleepTrackerApp functions = new SleepTrackerApp();

    @Test
    void testMinSession() {
        List<SleepingSession> s = Arrays.asList(
                new SleepingSession("01.10.25 21:50", "02.10.25 05:30", "BAD")
                , new SleepingSession("02.10.25 12:00", "02.10.25 12:15", "GOOD")
                , new SleepingSession("02.10.25 22:50", "03.10.25 05:20", "NORMAL"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(1).apply(s);
        assertEquals(15, result.getResult());
    }

    @Test
    void testMinSessionIfHaveTowSameSessionByTimes() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "02.10.25 00:00", "BAD"),
                new SleepingSession("02.10.25 21:00", "03.10.25 00:00", "BAD"),
                new SleepingSession("04.10.25 21:00", "05.10.25 06:00", "BAD"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(1).apply(s);
        assertEquals(180, result.getResult());
    }

    @Test
    void testMaxSession() {
        List<SleepingSession> s = Arrays.asList(
                new SleepingSession("01.10.25 21:50", "02.10.25 05:30", "BAD")
                , new SleepingSession("02.10.25 12:00", "02.10.25 12:15", "GOOD")
                , new SleepingSession("02.10.25 22:50", "03.10.25 05:20", "NORMAL"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(2).apply(s);
        assertEquals(460, result.getResult());
    }

    @Test
    void maxSessionIfHaveTowSameSessionByTimes() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "02.10.25 00:00", "BAD"),
                new SleepingSession("03.10.25 21:00", "04.10.25 00:00", "BAD"),
                new SleepingSession("04.10.25 23:00", "05.10.25 00:00", "BAD"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(2).apply(s);
        assertEquals(180, result.getResult());
    }

    @Test
    void testMiddleSession() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "02.10.25 00:00", "BAD"),
                new SleepingSession("03.10.25 23:30", "04.10.25 07:30", "GOOD"),
                new SleepingSession("04.10.25 22:00", "05.10.25 06:00", "BAD"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(3).apply(s);
        AtomicInteger d = new AtomicInteger();
        List<Duration> durations = s.stream()
                .map(session -> Duration.between(session.startSleep, session.endSleep))
                .peek(duration -> d.set(Math.toIntExact(duration.toMinutes())))
                .toList();
        long middle = d.get() / durations.size();

        assertEquals(middle, result.getResult());
    }

    @Test
    void testCountOfBadSessions() {
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(4).apply(sessions);
        long count = 5;
        assertEquals(count, result.getResult());
    }

    @Test
    void testCountOFZeroBadSession() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "02.10.25 00:00", "NORMAL"),
                new SleepingSession("03.10.25 23:30", "04.10.25 07:30", "GOOD"),
                new SleepingSession("04.10.25 22:00", "05.10.25 06:00", "NORMAL"));
        long count = 0;
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(4).apply(s);
        assertEquals(count, result.getResult());
    }

    @Test
    void testUnsleepCount() {
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(5).apply(sessions);
        long count = 1;
        assertEquals(count, result.getResult());
    }

    @Test
    void testUnsleepCountIfNoSleepsNight() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "01.10.25 23:10", "NORMAL"),
                new SleepingSession("02.10.25 12:30", "02.10.25 18:30", "GOOD"),
                new SleepingSession("03.10.25 17:00", "03.10.25 23:50", "NORMAL"),
                new SleepingSession("04.10.25 07:00", "04.10.25 11:00", "NORMAL"));
        long count = 3;
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(5).apply(s);
        assertEquals(count, result.getResult());
    }

    @Test
    void testUnsleepCountIfTransitionToNextMonth() {
        List<SleepingSession> s = List.of(
                new SleepingSession("29.10.25 21:00", "30.10.25 05:10", "NORMAL"),
                new SleepingSession("30.10.25 10:30", "30.10.25 18:30", "GOOD"),
                new SleepingSession("30.10.25 22:00", "31.10.25 06:50", "NORMAL"),
                new SleepingSession("31.10.25 23:00", "01.11.25 06:00", "NORMAL"),
                new SleepingSession("02.11.25 01:00", "02.11.25 06:00", "NORMAL"));
        long count = 0;
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(5).apply(s);
        assertEquals(count, result.getResult());


    }

    @Test
    void testUnsleepCountIfTransitionToNextYear() {
        List<SleepingSession> s = List.of(
                new SleepingSession("29.12.25 21:00", "30.12.25 05:10", "NORMAL"),
                new SleepingSession("30.12.25 10:30", "30.12.25 18:30", "GOOD"),
                new SleepingSession("30.12.25 22:00", "31.12.25 06:50", "NORMAL"),
                new SleepingSession("31.12.25 23:00", "01.01.26 06:00", "NORMAL"));
        long count = 0;
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(5).apply(s);
        assertEquals(count, result.getResult());
    }

    @Test
    void testUserClassificationLark() {
        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(6).apply(sessions);

        assertEquals("Жаворонок", result.getResult());
    }

    @Test
    void testUserClassificationOwl() {
        List<SleepingSession> s = List.of(
                new SleepingSession("29.12.25 23:30", "30.12.25 09:10", "NORMAL"),
                new SleepingSession("30.12.25 10:30", "30.12.25 18:30", "GOOD"),
                new SleepingSession("30.12.25 23:50", "31.12.25 10:50", "NORMAL"),
                new SleepingSession("31.12.25 23:00", "01.01.26 06:00", "NORMAL"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(6).apply(s);
        assertEquals("Сова", result.getResult());
    }

    @Test
    void testUserClassificationPigeon() {
        List<SleepingSession> s = List.of(
                new SleepingSession("29.12.25 21:30", "30.12.25 05:10", "NORMAL"),
                new SleepingSession("30.12.25 10:30", "30.12.25 18:30", "GOOD"),
                new SleepingSession("30.12.25 21:50", "31.12.25 04:50", "NORMAL"),
                new SleepingSession("31.12.25 23:10", "01.01.26 10:00", "NORMAL"),
                new SleepingSession("02.01.26 00:10", "02.01.26 09:35", "NORMAL"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(6).apply(s);
        assertEquals("Голубь", result.getResult());
    }

    @Test
    void testUserClassificationIfZeroNightSleeping() {
        List<SleepingSession> s = List.of(
                new SleepingSession("01.10.25 21:00", "01.10.25 23:10", "NORMAL"),
                new SleepingSession("02.10.25 12:30", "02.10.25 18:30", "GOOD"),
                new SleepingSession("03.10.25 17:00", "03.10.25 23:50", "NORMAL"),
                new SleepingSession("04.10.25 07:00", "04.10.25 11:00", "NORMAL"));

        SleepAnalysisResult result = (SleepAnalysisResult) functions.getFunctionList().get(6).apply(s);
        assertEquals("Голубь", result.getResult());
    }
}