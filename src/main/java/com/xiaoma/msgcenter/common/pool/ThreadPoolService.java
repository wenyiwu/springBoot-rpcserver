package com.xiaoma.msgcenter.common.pool;

import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadPoolService {

    private static ThreadPoolExecutor taskPool = null;

    private static BlockingQueue<Runnable> taskQueue = null;

    private final static int THREAD_POOL_CORE_SIZE = 16;
    private final static int THREAD_POOL_QUEUE_SIZE = 32;
    private final static int THREAD_POOL_MAX_SIZE = 32;
    private final static long THREAD_KEEP_ALIVE_TIME = 60;

    public ThreadPoolService() {
        taskQueue = new LinkedBlockingQueue<Runnable>(THREAD_POOL_QUEUE_SIZE);

        taskPool = new ThreadPoolExecutor(THREAD_POOL_CORE_SIZE, THREAD_POOL_MAX_SIZE, THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS, taskQueue,
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public ThreadPoolExecutor getPool() {
        return taskPool;
    }

    public void execute(Runnable runnable) {
        taskPool.execute(runnable);
    }

}