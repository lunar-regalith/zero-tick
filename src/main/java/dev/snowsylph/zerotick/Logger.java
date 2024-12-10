package dev.snowsylph.zerotick;

public class Logger {
    private static final String LOG_TAG = "[ZeroTick]: ";

    public static void info(String msg) {
        System.out.println(LOG_TAG + msg);
    }

    public static void err(String msg) {
        System.err.println(LOG_TAG + msg);
    }
}
