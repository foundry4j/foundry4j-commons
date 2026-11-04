package io.github.foundry4j.commons.concurrent;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public final class JucUtils {

    private JucUtils() {
    }

    // ==================== CountDownLatch ====================

    /// 创建 CountDownLatch
    ///
    /// @param count 倒计数值
    /// @return 新的 CountDownLatch 实例
    public static CountDownLatch newCountDownLatch(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than 0");
        }
        return new CountDownLatch(count);
    }

    /// 将 CountDownLatch 计数减一
    ///
    /// @param latch CountDownLatch 实例
    public static void countDown(CountDownLatch latch) {
        Objects.requireNonNull(latch, "latch must not be null");
        latch.countDown();
    }

    /// 等待 CountDownLatch 计数归零
    ///
    /// @param latch CountDownLatch 实例
    public static void await(CountDownLatch latch) {
        Objects.requireNonNull(latch, "latch must not be null");
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("CountDownLatch await interrupted", e);
        }
    }

    /// 等待 CountDownLatch 计数归零，带超时
    ///
    /// @param latch   CountDownLatch 实例
    /// @param timeout 最大等待时间
    /// @param unit    时间单位
    /// @return 是否在超时前计数归零
    public static boolean await(CountDownLatch latch, long timeout, TimeUnit unit) {
        Objects.requireNonNull(latch, "latch must not be null");
        Objects.requireNonNull(unit, "unit must not be null");

        try {
            return latch.await(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("CountDownLatch await interrupted", e);
        }
    }

    /// 获取 CountDownLatch 当前计数值
    ///
    /// @param latch CountDownLatch 实例
    /// @return 剩余计数
    public static long getCount(CountDownLatch latch) {
        Objects.requireNonNull(latch, "latch must not be null");
        return latch.getCount();
    }

    // ==================== CyclicBarrier ====================

    /// 创建 CyclicBarrier
    ///
    /// @param parties 参与方数量
    /// @return 新的 CyclicBarrier 实例
    public static CyclicBarrier newCyclicBarrier(int parties) {
        if (parties <= 0) {
            throw new IllegalArgumentException("parties must be greater than 0");
        }
        return new CyclicBarrier(parties);
    }

    /// 创建带屏障动作的 CyclicBarrier
    ///
    /// @param parties       参与方数量
    /// @param barrierAction 所有线程到达屏障后执行的动作
    /// @return 新的 CyclicBarrier 实例
    public static CyclicBarrier newCyclicBarrier(int parties, Runnable barrierAction) {
        if (parties <= 0) {
            throw new IllegalArgumentException("parties must be greater than 0");
        }
        return new CyclicBarrier(parties, barrierAction);
    }

    /// 等待所有线程到达屏障
    ///
    /// @param barrier CyclicBarrier 实例
    /// @return 到达序号
    public static int await(CyclicBarrier barrier) {
        Objects.requireNonNull(barrier, "barrier must not be null");
        try {
            return barrier.await();
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("CyclicBarrier await failed", e);
        }
    }

    /// 等待所有线程到达屏障，带超时
    ///
    /// @param barrier CyclicBarrier 实例
    /// @param timeout 最大等待时间
    /// @param unit    时间单位
    /// @return 到达序号
    public static int await(CyclicBarrier barrier, long timeout, TimeUnit unit) {
        Objects.requireNonNull(barrier, "barrier must not be null");
        Objects.requireNonNull(unit, "unit must not be null");

        try {
            return barrier.await(timeout, unit);
        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("CyclicBarrier await failed", e);
        }
    }

    /// 获取当前正在等待的线程数
    ///
    /// @param barrier CyclicBarrier 实例
    /// @return 正在等待的线程数
    public static int getNumberWaiting(CyclicBarrier barrier) {
        Objects.requireNonNull(barrier, "barrier must not be null");
        return barrier.getNumberWaiting();
    }

    /// 获取屏障参与方总数
    ///
    /// @param barrier CyclicBarrier 实例
    /// @return 参与方数量
    public static int getParties(CyclicBarrier barrier) {
        Objects.requireNonNull(barrier, "barrier must not be null");
        return barrier.getParties();
    }

    // ==================== Semaphore ====================

    /// 创建信号量
    ///
    /// @param permits 许可数量
    /// @return 新的 Semaphore 实例
    public static Semaphore newSemaphore(int permits) {
        if (permits < 0) {
            throw new IllegalArgumentException("permits must not be negative");
        }
        return new Semaphore(permits);
    }

    /// 创建公平信号量
    ///
    /// @param permits 许可数量
    /// @return 新的公平 Semaphore 实例
    public static Semaphore newFairSemaphore(int permits) {
        if (permits < 0) {
            throw new IllegalArgumentException("permits must not be negative");
        }
        return new Semaphore(permits, true);
    }

    /// 获取一个许可（阻塞）
    ///
    /// @param semaphore Semaphore 实例
    public static void acquire(Semaphore semaphore) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Semaphore acquire interrupted", e);
        }
    }

    /// 获取指定数量的许可（阻塞）
    ///
    /// @param semaphore Semaphore 实例
    /// @param permits   要获取的许可数
    public static void acquire(Semaphore semaphore, int permits) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        try {
            semaphore.acquire(permits);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Semaphore acquire interrupted", e);
        }
    }

    /// 尝试在指定时间内获取一个许可
    ///
    /// @param semaphore Semaphore 实例
    /// @param timeout   最大等待时间
    /// @param unit      时间单位
    /// @return 是否成功获取
    public static boolean tryAcquire(Semaphore semaphore, long timeout, TimeUnit unit) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        Objects.requireNonNull(unit, "unit must not be null");

        try {
            return semaphore.tryAcquire(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Semaphore tryAcquire interrupted", e);
        }
    }

    /// 尝试在指定时间内获取一个许可
    ///
    /// @param semaphore Semaphore 实例
    /// @param permits   获取的许可数
    /// @param timeout   最大等待时间
    /// @param unit      时间单位
    /// @return 是否成功获取
    public static boolean tryAcquire(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
        if (semaphore == null) {
            throw new NullPointerException("semaphore must not be null");
        }
        if (permits < 0) {
            throw new IllegalArgumentException("permits must not be negative");
        }
        if (unit == null) {
            throw new NullPointerException("timeUnit must not be null");
        }

        try {
            return semaphore.tryAcquire(permits, timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /// 释放一个许可
    ///
    /// @param semaphore Semaphore 实例
    public static void release(Semaphore semaphore) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        semaphore.release();
    }

    /// 释放指定数量的许可
    ///
    /// @param semaphore Semaphore 实例
    /// @param permits   要释放的许可数
    public static void release(Semaphore semaphore, int permits) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        semaphore.release(permits);
    }

    /// 获取当前可用许可数
    ///
    /// @param semaphore Semaphore 实例
    /// @return 可用许可数
    public static int availablePermits(Semaphore semaphore) {
        Objects.requireNonNull(semaphore, "semaphore must not be null");
        return semaphore.availablePermits();
    }
}