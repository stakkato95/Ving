package com.github.stakkato95.ving.os;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Artyom on 17.01.2015.
 */
public class ZExecutor {

    private static final int CPU_COUNT;
    private static final ExecutorService sExecutor;

    static {
        CPU_COUNT = Runtime.getRuntime().availableProcessors();
        sExecutor = new ThreadPoolExecutor(CPU_COUNT, CPU_COUNT, 0, TimeUnit.NANOSECONDS, new LIFOLinkedBlockingDeque<Runnable>());
    }

    public void execute(@NonNull Runnable runnable) {
        sExecutor.execute(runnable);
    }

}
