package com.pan.society.know.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class FutureExample {

    static class MyCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "Done";
        }
    }

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future1 = executorService.submit(new MyCallable());
        Future<String> future2 = executorService.submit(new MyCallable());
        log.info("do something in main");
        //Thread.sleep(1000);
        String result1 = future1.get();
        String result2 = future2.get();
        log.info("result1：{}", result1);
        log.info("result2：{}", result2);
    }
}
