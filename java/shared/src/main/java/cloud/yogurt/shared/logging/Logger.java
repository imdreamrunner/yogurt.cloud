package cloud.yogurt.shared.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger used by all Yogurt classes.
 */
public class Logger {
    private String name;

    private Logger(String name) {
        this.name = name;
    }

    private static String getLogLevelName(Level level) {
        return level.name();
    }

    private static String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        return simpleDateFormat.format(now);
    }

    public synchronized void log(Level level, String tag, String message) {
        StringBuilder logString = new StringBuilder();
        logString.append(getCurrentTime());
        logString.append(" ");
        logString.append(getLogLevelName(level));
        logString.append(" ");
        logString.append(name);
        logString.append(" ");
        if (tag != null) {
            logString.append(" [");
            logString.append(tag);
            logString.append("] ");
        }
        logString.append(message);
        System.err.println(logString.toString());
    }

    public void debug(String tag, String message) {
        log(Level.DEBUG, tag, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, null, message);
    }

    public void info(String tag, String message) {
        log(Level.INFO, tag, message);
    }

    public void info(String message) {
        log(Level.INFO, null, message);
    }

    public void warn(String tag, String message) {
        log(Level.WARNING, tag, message);
    }

    public void warn(String message) {
        log(Level.WARNING, null, message);
    }

    public void error(String tag, String message) {
        log(Level.ERROR, tag, message);
    }

    public void error(String message) {
        log(Level.ERROR, null, message);
    }

    public static Logger getLogger(String name) {
        return new Logger(name);
    }
}
