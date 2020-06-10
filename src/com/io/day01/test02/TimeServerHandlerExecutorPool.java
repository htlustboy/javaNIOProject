package com.io.day01.test02;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimeServerHandlerExecutorPool {

    private ExecutorService executorService;

    TimeServerHandlerExecutorPool(int maxPoolSize,int queueSize){
        executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
    }

    public void executor(Runnable task){
        executorService.execute(task);
    }
}
