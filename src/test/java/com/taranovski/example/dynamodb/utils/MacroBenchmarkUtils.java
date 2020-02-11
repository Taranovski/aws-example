package com.taranovski.example.dynamodb.utils;

import java.util.function.Supplier;

/**
 * Created by Alyx on 11.02.2020.
 */
public class MacroBenchmarkUtils {
    public static void measureAndLogExecutionTime(RunnableWithException runnable) throws Exception {
        long currentTimeMillis = System.currentTimeMillis();


        runnable.run();

        long currentTimeMillis1 = System.currentTimeMillis();

        long duration = currentTimeMillis1 - currentTimeMillis;

        System.out.println("time: " + duration);
    }

    public static <T> T measureAndLogExecutionTime(Supplier<T> supplier) {
        long currentTimeMillis = System.currentTimeMillis();

        T t = supplier.get();

        long currentTimeMillis1 = System.currentTimeMillis();

        long duration = currentTimeMillis1 - currentTimeMillis;

        System.out.println("time: " + duration);

        return t;
    }

}
