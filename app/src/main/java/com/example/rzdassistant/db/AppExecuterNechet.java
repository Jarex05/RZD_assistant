package com.example.rzdassistant.db;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecuterNechet {
    private static AppExecuterNechet instanceNechet;
    private final Executor MainIoNechet;
    private final Executor SubIoNechet;

    public AppExecuterNechet(Executor mainIoNechet, Executor subIoNechet) {
        MainIoNechet = mainIoNechet;
        SubIoNechet = subIoNechet;
    }

    public static AppExecuterNechet getInstance() {

        if (instanceNechet == null) instanceNechet = new AppExecuterNechet(new MainThreadHandlerNechet(), Executors.newSingleThreadExecutor());
        return instanceNechet;
    }

    public static class MainThreadHandlerNechet implements Executor {
        private Handler mainHandlerNechet = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            mainHandlerNechet.post(command);
        }
    }

    public Executor getMainIoNechet() {
        return MainIoNechet;
    }

    public Executor getSubIoNechet() {
        return SubIoNechet;
    }
}
