package cloud.yogurt.shared.time;

/**
 * Helper class to execute a handler after a duration of time.
 */
public class SetTimeout extends Thread {

    private long duration;
    private TimeoutHandler timeoutHandler;

    public SetTimeout(TimeoutHandler timeoutHandler, long duration) {
        this.timeoutHandler = timeoutHandler;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(duration);
            this.timeoutHandler.handleTimeout();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setTimeout(TimeoutHandler timeoutHandler, long duration) {
        new SetTimeout(timeoutHandler, duration).start();
    }

}
