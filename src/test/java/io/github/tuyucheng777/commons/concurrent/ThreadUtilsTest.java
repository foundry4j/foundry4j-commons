package io.github.tuyucheng777.commons.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ThreadUtilsTest {

    // ==================== sleep ====================

    @Test
    void sleep_millis_shouldPauseThread() {
        long start = System.currentTimeMillis();
        ThreadUtils.sleep(100);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 90, "Should have slept at least ~100ms");
    }

    @Test
    void sleep_millis_negativeShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> ThreadUtils.sleep(-1));
    }

    @Test
    void sleep_withTimeUnit_shouldPauseThread() {
        long start = System.currentTimeMillis();
        ThreadUtils.sleep(100, TimeUnit.MILLISECONDS);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 90, "Should have slept at least ~100ms");
    }

    @Test
    void sleep_withTimeUnit_negativeShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadUtils.sleep(-1, TimeUnit.MILLISECONDS));
    }

    @Test
    void sleep_withTimeUnit_nullUnitShouldThrow() {
        assertThrows(NullPointerException.class,
                () -> ThreadUtils.sleep(100, null));
    }

    // ==================== join ====================

    @Test
    void join_shouldWaitForThreadCompletion() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            ThreadUtils.sleep(50);
            executed.set(true);
        });
        thread.start();
        ThreadUtils.join(thread);
        assertTrue(executed.get(), "Thread should have completed before join returns");
    }

    @Test
    void join_nullThreadShouldThrow() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.join(null));
    }

    @Test
    void join_withTimeout_shouldReturnAfterTimeout() {
        Thread thread = new Thread(() -> ThreadUtils.sleep(5000));
        thread.start();
        long start = System.currentTimeMillis();
        ThreadUtils.join(thread, 100);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed < 1000, "Should return after timeout, not wait for thread");
        thread.interrupt();
    }

    @Test
    void join_withTimeout_negativeShouldThrow() {
        Thread thread = new Thread();
        assertThrows(IllegalArgumentException.class,
                () -> ThreadUtils.join(thread, -1));
    }

    // ==================== startAndJoin ====================

    @Test
    void startAndJoin_shouldStartAndComplete() {
        AtomicBoolean executed = new AtomicBoolean(false);
        Thread thread = new Thread(() -> executed.set(true));
        ThreadUtils.startAndJoin(thread);
        assertTrue(executed.get());
        assertFalse(thread.isAlive(), "Thread should have terminated");
    }

    @Test
    void startAndJoin_nullThreadShouldThrow() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.startAndJoin(null));
    }

    // ==================== newThread / newDaemonThread ====================

    @Test
    void newThread_shouldCreateNamedThread() {
        Thread thread = ThreadUtils.newThread("test-thread", () -> {
        });
        assertEquals("test-thread", thread.getName());
        assertFalse(thread.isDaemon());
        assertFalse(thread.isAlive(), "Thread should not be started");
    }

    @Test
    void newThread_nullNameShouldUseDefault() {
        Thread thread = ThreadUtils.newThread(null, () -> {
        });
        assertNotNull(thread.getName());
    }

    @Test
    void newThread_nullTaskShouldThrow() {
        assertThrows(NullPointerException.class,
                () -> ThreadUtils.newThread("name", null));
    }

    @Test
    void newDaemonThread_shouldCreateDaemonThread() {
        Thread thread = ThreadUtils.newDaemonThread("daemon-thread", () -> {
        });
        assertEquals("daemon-thread", thread.getName());
        assertTrue(thread.isDaemon());
    }

    // ==================== submit ====================

    @Test
    void submit_callable_shouldReturnResult() throws Exception {
        ExecutorService executor = ThreadPoolUtils.newSingleThreadPool("test-submit");
        try {
            Future<String> future = ThreadUtils.submit(executor, (Callable<String>) () -> "hello");
            assertEquals("hello", future.get());
        } finally {
            ThreadPoolUtils.shutdown(executor);
        }
    }

    @Test
    void submit_runnable_shouldComplete() throws Exception {
        ExecutorService executor = ThreadPoolUtils.newSingleThreadPool("test-submit-r");
        try {
            AtomicBoolean executed = new AtomicBoolean(false);
            Future<?> future = ThreadUtils.submit(executor, (Runnable) () -> executed.set(true));
            future.get();
            assertTrue(executed.get());
        } finally {
            ThreadPoolUtils.shutdown(executor);
        }
    }

    @Test
    void submit_nullExecutorShouldThrow() {
        assertThrows(NullPointerException.class,
                () -> ThreadUtils.submit(null, (Runnable) () -> {
                }));
    }

    @Test
    void submit_nullTaskShouldThrow() {
        ExecutorService executor = ThreadPoolUtils.newSingleThreadPool("test-null");
        try {
            assertThrows(NullPointerException.class,
                    () -> ThreadUtils.submit(executor, (Runnable) null));
        } finally {
            ThreadPoolUtils.shutdown(executor);
        }
    }

    // ==================== state queries ====================

    @Test
    void isAlive_runningThreadShouldReturnTrue() {
        Thread thread = new Thread(() -> ThreadUtils.sleep(2000));
        thread.start();
        assertTrue(ThreadUtils.isAlive(thread));
        thread.interrupt();
    }

    @Test
    void isAlive_terminatedThreadShouldReturnFalse() {
        Thread thread = new Thread(() -> {
        });
        thread.start();
        ThreadUtils.join(thread);
        assertFalse(ThreadUtils.isAlive(thread));
    }

    @Test
    void isAlive_nullThreadShouldThrow() {
        assertThrows(NullPointerException.class, () -> ThreadUtils.isAlive(null));
    }

    @Test
    void getState_shouldReturnCorrectState() {
        Thread thread = new Thread(() -> ThreadUtils.sleep(2000));
        assertEquals(Thread.State.NEW, ThreadUtils.getState(thread));
        thread.start();
        ThreadUtils.sleep(50);
        assertEquals(Thread.State.TIMED_WAITING, ThreadUtils.getState(thread));
        thread.interrupt();
    }

    @Test
    void isDaemon_shouldReturnCorrectValue() {
        Thread normal = ThreadUtils.newThread("normal", () -> {
        });
        assertFalse(ThreadUtils.isDaemon(normal));

        Thread daemon = ThreadUtils.newDaemonThread("daemon", () -> {
        });
        assertTrue(ThreadUtils.isDaemon(daemon));
    }

    @Test
    void currentThreadName_shouldReturnCurrentName() {
        String name = ThreadUtils.currentThreadName();
        assertNotNull(name);
        assertEquals(Thread.currentThread().getName(), name);
    }

    @Test
    void currentThreadId_shouldReturnCurrentId() {
        long id = ThreadUtils.currentThreadId();
        assertEquals(Thread.currentThread().getId(), id);
    }
}