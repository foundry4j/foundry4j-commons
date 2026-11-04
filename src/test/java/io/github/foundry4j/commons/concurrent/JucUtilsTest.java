package io.github.foundry4j.commons.concurrent;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JucUtilsTest {

    // ==================== CountDownLatch ====================

    @Test
    void newCountDownLatch_shouldCreateWithCount() {
        CountDownLatch latch = JucUtils.newCountDownLatch(3);
        assertEquals(3, latch.getCount());
    }

    @Test
    void newCountDownLatch_invalidCountShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> JucUtils.newCountDownLatch(0));
        assertThrows(IllegalArgumentException.class, () -> JucUtils.newCountDownLatch(-1));
    }

    @Test
    void countDown_shouldDecreaseCount() {
        CountDownLatch latch = JucUtils.newCountDownLatch(2);
        JucUtils.countDown(latch);
        assertEquals(1, JucUtils.getCount(latch));
    }

    @Test
    void countDown_nullShouldThrow() {
        assertThrows(NullPointerException.class, () -> JucUtils.countDown(null));
    }

    @Test
    void await_shouldBlockUntilZero() throws Exception {
        CountDownLatch latch = JucUtils.newCountDownLatch(1);
        AtomicInteger result = new AtomicInteger(0);

        Thread thread = new Thread(() -> {
            JucUtils.await(latch);
            result.set(42);
        });
        thread.start();

        ThreadUtils.sleep(100);
        assertEquals(0, result.get(), "Should still be blocked");

        JucUtils.countDown(latch);
        thread.join(2000);
        assertEquals(42, result.get(), "Should unblock after count reaches zero");
    }

    @Test
    void await_withTimeout_shouldReturnFalseOnTimeout() {
        CountDownLatch latch = JucUtils.newCountDownLatch(1);
        boolean completed = JucUtils.await(latch, 100, TimeUnit.MILLISECONDS);
        assertFalse(completed, "Should timeout before count reaches zero");
    }

    @Test
    void await_withTimeout_shouldReturnTrueWhenCompleted() {
        CountDownLatch latch = JucUtils.newCountDownLatch(1);
        latch.countDown();
        boolean completed = JucUtils.await(latch, 1, TimeUnit.SECONDS);
        assertTrue(completed);
    }

    @Test
    void getCount_shouldReturnCurrentCount() {
        CountDownLatch latch = JucUtils.newCountDownLatch(5);
        assertEquals(5, JucUtils.getCount(latch));
        latch.countDown();
        latch.countDown();
        assertEquals(3, JucUtils.getCount(latch));
    }

    // ==================== CyclicBarrier ====================

    @Test
    void newCyclicBarrier_shouldCreateWithParties() {
        CyclicBarrier barrier = JucUtils.newCyclicBarrier(3);
        assertEquals(3, JucUtils.getParties(barrier));
        assertEquals(0, JucUtils.getNumberWaiting(barrier));
    }

    @Test
    void newCyclicBarrier_invalidPartiesShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> JucUtils.newCyclicBarrier(0));
        assertThrows(IllegalArgumentException.class, () -> JucUtils.newCyclicBarrier(-1));
    }

    @Test
    void newCyclicBarrier_withAction_shouldCreate() {
        AtomicInteger actionCount = new AtomicInteger(0);
        CyclicBarrier barrier = JucUtils.newCyclicBarrier(2, actionCount::incrementAndGet);
        assertEquals(2, JucUtils.getParties(barrier));
    }

    @Test
    void await_shouldReleaseAllThreads() throws Exception {
        CyclicBarrier barrier = JucUtils.newCyclicBarrier(2);
        AtomicInteger arrived = new AtomicInteger(0);

        Thread t1 = new Thread(() -> {
            JucUtils.await(barrier);
            arrived.incrementAndGet();
        });
        Thread t2 = new Thread(() -> {
            JucUtils.await(barrier);
            arrived.incrementAndGet();
        });

        t1.start();
        ThreadUtils.sleep(100);
        assertEquals(1, JucUtils.getNumberWaiting(barrier));

        t2.start();
        t1.join(2000);
        t2.join(2000);
        assertEquals(2, arrived.get());
    }

    @Test
    void await_nullShouldThrow() {
        assertThrows(NullPointerException.class, () -> JucUtils.await((CyclicBarrier) null));
    }

    @Test
    void getNumberWaiting_shouldTrackWaitingThreads() throws Exception {
        CyclicBarrier barrier = JucUtils.newCyclicBarrier(3);

        Thread t1 = new Thread(() -> JucUtils.await(barrier));
        t1.start();
        ThreadUtils.sleep(100);
        assertEquals(1, JucUtils.getNumberWaiting(barrier));

        barrier.reset();
        t1.interrupt();
        t1.join(1000);
    }

    // ==================== Semaphore ====================

    @Test
    void newSemaphore_shouldCreateWithPermits() {
        Semaphore semaphore = JucUtils.newSemaphore(3);
        assertEquals(3, JucUtils.availablePermits(semaphore));
    }

    @Test
    void newSemaphore_negativePermitsShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> JucUtils.newSemaphore(-1));
    }

    @Test
    void newFairSemaphore_shouldCreate() {
        Semaphore semaphore = JucUtils.newFairSemaphore(5);
        assertEquals(5, JucUtils.availablePermits(semaphore));
    }

    @Test
    void acquire_shouldDecreasePermits() {
        Semaphore semaphore = JucUtils.newSemaphore(3);
        JucUtils.acquire(semaphore);
        assertEquals(2, JucUtils.availablePermits(semaphore));
    }

    @Test
    void acquire_multiplePermits_shouldDecreaseByAmount() {
        Semaphore semaphore = JucUtils.newSemaphore(5);
        JucUtils.acquire(semaphore, 3);
        assertEquals(2, JucUtils.availablePermits(semaphore));
    }

    @Test
    void acquire_nullShouldThrow() {
        assertThrows(NullPointerException.class, () -> JucUtils.acquire(null));
    }

    @Test
    void release_shouldIncreasePermits() {
        Semaphore semaphore = JucUtils.newSemaphore(1);
        JucUtils.acquire(semaphore);
        assertEquals(0, JucUtils.availablePermits(semaphore));
        JucUtils.release(semaphore);
        assertEquals(1, JucUtils.availablePermits(semaphore));
    }

    @Test
    void release_multiplePermits_shouldIncreaseByAmount() {
        Semaphore semaphore = JucUtils.newSemaphore(0);
        JucUtils.release(semaphore, 3);
        assertEquals(3, JucUtils.availablePermits(semaphore));
    }

    @Test
    void tryAcquire_shouldReturnTrueWhenAvailable() {
        Semaphore semaphore = JucUtils.newSemaphore(1);
        boolean acquired = JucUtils.tryAcquire(semaphore, 100, TimeUnit.MILLISECONDS);
        assertTrue(acquired);
        assertEquals(0, JucUtils.availablePermits(semaphore));
    }

    @Test
    void tryAcquire_shouldReturnFalseWhenUnavailable() {
        Semaphore semaphore = JucUtils.newSemaphore(0);
        boolean acquired = JucUtils.tryAcquire(semaphore, 100, TimeUnit.MILLISECONDS);
        assertFalse(acquired);
    }

    @Test
    void tryAcquire_withPermits_shouldReturnTrueWhenAvailable() {
        Semaphore semaphore = JucUtils.newSemaphore(3);
        boolean acquired = JucUtils.tryAcquire(semaphore, 2, 100, TimeUnit.MILLISECONDS);
        assertTrue(acquired);
        assertEquals(1, JucUtils.availablePermits(semaphore));
    }

    @Test
    void tryAcquire_withPermits_shouldReturnFalseWhenInsufficient() {
        Semaphore semaphore = JucUtils.newSemaphore(1);
        boolean acquired = JucUtils.tryAcquire(semaphore, 3, 100, TimeUnit.MILLISECONDS);
        assertFalse(acquired);
        assertEquals(1, JucUtils.availablePermits(semaphore));
    }

    @Test
    void tryAcquire_nullSemaphoreShouldThrow() {
        assertThrows(NullPointerException.class,
                () -> JucUtils.tryAcquire(null, 100, TimeUnit.MILLISECONDS));
    }

    @Test
    void tryAcquire_negativePermitsShouldThrow() {
        Semaphore semaphore = JucUtils.newSemaphore(3);
        assertThrows(IllegalArgumentException.class,
                () -> JucUtils.tryAcquire(semaphore, -1, 100, TimeUnit.MILLISECONDS));
    }

    @Test
    void availablePermits_shouldReturnCurrentCount() {
        Semaphore semaphore = JucUtils.newSemaphore(5);
        assertEquals(5, JucUtils.availablePermits(semaphore));
        JucUtils.acquire(semaphore, 2);
        assertEquals(3, JucUtils.availablePermits(semaphore));
    }

    @Test
    void availablePermits_nullShouldThrow() {
        assertThrows(NullPointerException.class, () -> JucUtils.availablePermits(null));
    }
}