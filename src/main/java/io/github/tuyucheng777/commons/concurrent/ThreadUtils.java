package io.github.tuyucheng777.commons.concurrent;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class ThreadUtils {

    private ThreadUtils() {
    }

    public static void sleep(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("millis must not be negative");
        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread sleep interrupted", e);
        }
    }

    public static void sleep(long timeout, TimeUnit unit) {
        Objects.requireNonNull(unit, "unit must not be null");
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must not be negative");
        }
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread sleep interrupted", e);
        }
    }

    public static void join(Thread thread) {
        Objects.requireNonNull(thread, "thread must not be null");
        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread join interrupted", e);
        }
    }

    public static void join(Thread thread, long millis) {
        Objects.requireNonNull(thread, "thread must not be null");
        if (millis < 0) {
            throw new IllegalArgumentException("millis must not be negative");
        }
        try {
            thread.join(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread join interrupted", e);
        }
    }

    public static void startAndJoin(Thread thread) {
        Objects.requireNonNull(thread, "thread must not be null");
        thread.start();
        join(thread);
    }

    public static Thread newThread(String name, Runnable task) {
        Objects.requireNonNull(task, "task must not be null");
        Thread thread = new Thread(task);
        if (name != null) {
            thread.setName(name);
        }
        return thread;
    }

    public static Thread newDaemonThread(String name, Runnable task) {
        Thread thread = newThread(name, task);
        thread.setDaemon(true);
        return thread;
    }

    public static <T> Future<T> submit(ExecutorService executor, Callable<T> task) {
        Objects.requireNonNull(executor, "executor must not be null");
        Objects.requireNonNull(task, "task must not be null");
        return executor.submit(task);
    }

    public static Future<?> submit(ExecutorService executor, Runnable task) {
        Objects.requireNonNull(executor, "executor must not be null");
        Objects.requireNonNull(task, "task must not be null");
        return executor.submit(task);
    }

    public static boolean isAlive(Thread thread) {
        Objects.requireNonNull(thread, "thread must not be null");
        return thread.isAlive();
    }

    public static Thread.State getState(Thread thread) {
        Objects.requireNonNull(thread, "thread must not be null");
        return thread.getState();
    }

    public static boolean isDaemon(Thread thread) {
        Objects.requireNonNull(thread, "thread must not be null");
        return thread.isDaemon();
    }

    public static String currentThreadName() {
        return Thread.currentThread().getName();
    }

    public static long currentThreadId() {
        return Thread.currentThread().getId();
    }
}