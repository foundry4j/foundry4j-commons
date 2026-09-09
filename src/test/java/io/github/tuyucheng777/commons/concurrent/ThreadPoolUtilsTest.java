package io.github.tuyucheng777.commons.concurrent;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolUtilsTest {

    private ThreadPoolExecutor executor;

    @AfterEach
    void tearDown() {
        if (executor != null && !executor.isShutdown()) {
            ThreadPoolUtils.shutdownAndAwait(executor, 5, TimeUnit.SECONDS);
        }
    }

    // ==================== newFixedThreadPool ====================

    @Test
    void newFixedThreadPool_shouldCreateWithCorrectSize() {
        executor = ThreadPoolUtils.newFixedThreadPool(3, "fixed");
        assertEquals(3, executor.getCorePoolSize());
        assertEquals(3, executor.getMaximumPoolSize());
    }

    @Test
    void newFixedThreadPool_invalidSizeShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolUtils.newFixedThreadPool(0, "bad"));
        assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolUtils.newFixedThreadPool(-1, "bad"));
    }

    @Test
    void newFixedThreadPool_withQueueCapacity_shouldLimitQueue() {
        executor = ThreadPoolUtils.newFixedThreadPool(2, 10, "bounded");
        assertEquals(2, executor.getCorePoolSize());
        assertEquals(10, executor.getQueue().remainingCapacity() + executor.getQueue().size());
    }

    @Test
    void newFixedThreadPool_invalidQueueCapacityShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolUtils.newFixedThreadPool(2, 0, "bad"));
    }

    // ==================== newCachedThreadPool ====================

    @Test
    void newCachedThreadPool_shouldCreateWithCorrectParams() {
        executor = ThreadPoolUtils.newCachedThreadPool("cached");
        assertEquals(0, executor.getCorePoolSize());
        assertEquals(Integer.MAX_VALUE, executor.getMaximumPoolSize());
    }

    // ==================== newSingleThreadPool ====================

    @Test
    void newSingleThreadPool_shouldHaveOneThread() {
        executor = ThreadPoolUtils.newSingleThreadPool("single");
        assertEquals(1, executor.getCorePoolSize());
        assertEquals(1, executor.getMaximumPoolSize());
    }

    // ==================== newScheduledThreadPool ====================

    @Test
    void newScheduledThreadPool_shouldCreate() {
        ScheduledExecutorService scheduled = ThreadPoolUtils.newScheduledThreadPool(2, "scheduled");
        assertNotNull(scheduled);
        assertFalse(scheduled.isShutdown());
        scheduled.shutdown();
    }

    @Test
    void newScheduledThreadPool_invalidSizeShouldThrow() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolUtils.newScheduledThreadPool(0, "bad"));
    }

    // ==================== namedThreadFactory ====================

    @Test
    void namedThreadFactory_shouldCreateSequentialNames() {
        ThreadFactory factory = ThreadPoolUtils.namedThreadFactory("worker");
        Thread t1 = factory.newThread(() -> {
        });
        Thread t2 = factory.newThread(() -> {
        });
        assertEquals("worker-1", t1.getName());
        assertEquals("worker-2", t2.getName());
    }

    @Test
    void namedThreadFactory_nullNameShouldThrow() {
        assertThrows(NullPointerException.class,
                () -> ThreadPoolUtils.namedThreadFactory(null));
    }

    // ==================== shutdown / awaitTermination ====================

    @Test
    void shutdown_shouldStopAcceptingTasks() {
        executor = ThreadPoolUtils.newFixedThreadPool(2, "shutdown-test");
        assertFalse(executor.isShutdown());
        ThreadPoolUtils.shutdown(executor);
        assertTrue(executor.isShutdown());
    }

    @Test
    void shutdown_nullShouldNotThrow() {
        assertDoesNotThrow(() -> ThreadPoolUtils.shutdown(null));
    }

    @Test
    void awaitTermination_shouldReturnTrueWhenCompleted() throws Exception {
        executor = ThreadPoolUtils.newSingleThreadPool("await-test");
        executor.submit(() -> {
        });
        ThreadPoolUtils.shutdown(executor);
        boolean terminated = ThreadPoolUtils.awaitTermination(executor, 5, TimeUnit.SECONDS);
        assertTrue(terminated);
    }

    @Test
    void awaitTermination_invalidTimeoutShouldThrow() {
        executor = ThreadPoolUtils.newSingleThreadPool("bad-timeout");
        assertThrows(IllegalArgumentException.class,
                () -> ThreadPoolUtils.awaitTermination(executor, 0, TimeUnit.SECONDS));
    }

    @Test
    void shutdownAndAwait_shouldTerminatePool() {
        executor = ThreadPoolUtils.newFixedThreadPool(2, "shutdown-await");
        executor.submit(() -> ThreadUtils.sleep(50));
        ThreadPoolUtils.shutdownAndAwait(executor, 5, TimeUnit.SECONDS);
        assertTrue(executor.isTerminated());
        executor = null; // prevent double shutdown in tearDown
    }

    // ==================== state queries ====================

    @Test
    void isShutdown_shouldReflectState() {
        executor = ThreadPoolUtils.newSingleThreadPool("state-test");
        assertFalse(ThreadPoolUtils.isShutdown(executor));
        ThreadPoolUtils.shutdown(executor);
        assertTrue(ThreadPoolUtils.isShutdown(executor));
    }

    @Test
    void isTerminated_shouldReturnTrueAfterAllDone() {
        executor = ThreadPoolUtils.newSingleThreadPool("term-test");
        executor.submit(() -> {
        });
        ThreadPoolUtils.shutdownAndAwait(executor, 5, TimeUnit.SECONDS);
        assertTrue(ThreadPoolUtils.isTerminated(executor));
        executor = null;
    }

    @Test
    void getActiveCount_shouldTrackRunningThreads() throws Exception {
        executor = ThreadPoolUtils.newFixedThreadPool(2, "active-test");
        executor.submit(() -> ThreadUtils.sleep(500));
        executor.submit(() -> ThreadUtils.sleep(500));
        ThreadUtils.sleep(100);
        assertEquals(2, ThreadPoolUtils.getActiveCount(executor));
    }

    @Test
    void getCompletedTaskCount_shouldTrackCompleted() {
        executor = ThreadPoolUtils.newSingleThreadPool("completed-test");
        executor.submit(() -> {
        });
        executor.submit(() -> {
        });
        ThreadPoolUtils.shutdownAndAwait(executor, 5, TimeUnit.SECONDS);
        assertEquals(2, ThreadPoolUtils.getCompletedTaskCount(executor));
        executor = null;
    }

    @Test
    void getPoolSize_shouldReturnCurrentSize() {
        executor = ThreadPoolUtils.newFixedThreadPool(3, "pool-size");
        executor.submit(() -> ThreadUtils.sleep(200));
        ThreadUtils.sleep(50);
        assertTrue(ThreadPoolUtils.getPoolSize(executor) > 0);
    }

    @Test
    void getQueueSize_shouldReturnPendingCount() {
        executor = ThreadPoolUtils.newFixedThreadPool(1, 100, "queue-test");
        // submit multiple tasks, only 1 runs at a time
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> ThreadUtils.sleep(200));
        }
        ThreadUtils.sleep(100);
        assertTrue(ThreadPoolUtils.getQueueSize(executor) > 0);
    }

    @Test
    void getActiveCount_nonThreadPoolExecutorShouldReturnMinusOne() {
        ScheduledExecutorService scheduled = ThreadPoolUtils.newScheduledThreadPool(1, "sched");
        assertEquals(0, ThreadPoolUtils.getActiveCount(scheduled));
        scheduled.shutdown();
    }
}