package it.unibo.smartgh.greenhouse;

public class Logger {
    public static void log(String msg) {
        System.out.println("[GreenhouseHTTPAdapter][" + System.currentTimeMillis() + "] " + msg);
    }
}
