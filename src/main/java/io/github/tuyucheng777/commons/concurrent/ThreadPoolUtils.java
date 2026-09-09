package io.github.tuyucheng777.commons.concurrent;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreadPoolUtils {

    private ThreadPoolUtils() {
    }

    /// 创建固定大小的线程池
    ///
    /// @param corePoolSize 核心线程数
    /// @param poolName     线程池名称前缀
    /// @return 配置好的线程池
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize, String poolName) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException("corePoolSize must be greater than 0");
        }
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                namedThreadFactory(poolName)
        );
    }

    /// 创建固定大小的线程池，指定队列容量
    ///
    /// @param corePoolSize  核心线程数
    /// @param queueCapacity 任务队列容量
    /// @param poolName      线程池名称前缀
    /// @return 配置好的线程池
    public static ThreadPoolExecutor newFixedThreadPool(int corePoolSize, int queueCapacity, String poolName) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException("corePoolSize must be greater than 0");
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("queueCapacity must be greater than 0");
        }
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueCapacity),
                namedThreadFactory(poolName)
        );
    }

    /// 创建缓存线程池（按需创建，空闲60秒回收）
    ///
    /// @param poolName 线程池名称前缀
    /// @return 配置好的缓存线程池
    public static ThreadPoolExecutor newCachedThreadPool(String poolName) {
        return new ThreadPoolExecutor(
                0,
                Integer.MAX_VALUE,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                namedThreadFactory(poolName)
        );
    }

    /// 创建单线程池
    ///
    /// @param poolName 线程池名称前缀
    /// @return 配置好的单线程池
    public static ThreadPoolExecutor newSingleThreadPool(String poolName) {
        return new ThreadPoolExecutor(
                1,
                1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                namedThreadFactory(poolName)
        );
    }

    /// 创建定时任务线程池
    ///
    /// @param corePoolSize 核心线程数
    /// @param poolName     线程池名称前缀
    /// @return 配置好的定时任务线程池
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, String poolName) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException("corePoolSize must be greater than 0");
        }
        return new ScheduledThreadPoolExecutor(
                corePoolSize,
                namedThreadFactory(poolName)
        );
    }

    /// 创建带命名前缀的线程工厂
    ///
    /// @param poolName 线程名称前缀
    /// @return 线程工厂实例
    public static ThreadFactory namedThreadFactory(String poolName) {
        Objects.requireNonNull(poolName, "poolName must not be null");

        AtomicInteger counter = new AtomicInteger(1);
        return r -> {
            Thread thread = new Thread(r);
            thread.setName(poolName + "-" + counter.getAndIncrement());
            return thread;
        };
    }

    /// 优雅关闭线程池
    ///
    /// @param executor 要关闭的线程池
    public static void shutdown(ExecutorService executor) {
        if (executor == null) {
            return;
        }
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
    }

    /// 等待线程池终止
    ///
    /// @param executor 线程池
    /// @param timeout  最大等待时间
    /// @param unit     时间单位
    /// @return 是否在超时前终止
    public static boolean awaitTermination(ExecutorService executor, long timeout, TimeUnit unit) {
        Objects.requireNonNull(executor, "executor must not be null");
        Objects.requireNonNull(unit, "unit must not be null");

        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must be greater than 0");
        }

        try {
            return executor.awaitTermination(timeout, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Await termination interrupted", e);
        }
    }

    /// 关闭线程池并等待终止，超时则强制关闭
    ///
    /// @param executor 线程池
    /// @param timeout  最大等待时间
    /// @param unit     时间单位
    public static void shutdownAndAwait(ExecutorService executor, long timeout, TimeUnit unit) {
        Objects.requireNonNull(executor, "executor must not be null");
        Objects.requireNonNull(unit, "unit must not be null");

        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout must be greater than 0");
        }

        shutdown(executor);

        try {
            if (!executor.awaitTermination(timeout, unit)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw new RuntimeException("Shutdown and await interrupted", e);
        }
    }

    /// 判断线程池是否已关闭
    ///
    /// @param executor 线程池
    /// @return 是否已调用 shutdown
    public static boolean isShutdown(ExecutorService executor) {
        Objects.requireNonNull(executor, "executor must not be null");
        return executor.isShutdown();
    }

    /// 判断线程池是否已终止
    ///
    /// @param executor 线程池
    /// @return 所有任务是否已完成
    public static boolean isTerminated(ExecutorService executor) {
        Objects.requireNonNull(executor, "executor must not be null");
        return executor.isTerminated();
    }

    /// 获取线程池当前活跃线程数
    ///
    /// @param executor 线程池
    /// @return 活跃线程数，非 ThreadPoolExecutor 返回 -1
    public static int getActiveCount(ExecutorService executor) {
        if (executor instanceof ThreadPoolExecutor pool) {
            return pool.getActiveCount();
        }
        return -1;
    }

    /// 获取线程池已完成任务数
    ///
    /// @param executor 线程池
    /// @return 已完成任务数，非 ThreadPoolExecutor 返回 -1
    public static long getCompletedTaskCount(ExecutorService executor) {
        if (executor instanceof ThreadPoolExecutor pool) {
            return pool.getCompletedTaskCount();
        }
        return -1L;
    }

    /// 获取线程池当前线程数
    ///
    /// @param executor 线程池
    /// @return 当前线程数，非 ThreadPoolExecutor 返回 -1
    public static int getPoolSize(ExecutorService executor) {
        if (executor instanceof ThreadPoolExecutor pool) {
            return pool.getPoolSize();
        }
        return -1;
    }

    /// 获取线程池队列中等待的任务数
    ///
    /// @param executor 线程池
    /// @return 队列大小，非 ThreadPoolExecutor 返回 -1
    public static int getQueueSize(ExecutorService executor) {
        if (executor instanceof ThreadPoolExecutor pool) {
            return pool.getQueue().size();
        }
        return -1;
    }
}