package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.time.TimeoutHandler;

public class MonitorTimeoutHandler implements TimeoutHandler {
    private static Logger log = Logger.getLogger(MonitorTimeoutHandler.class.getName());

    private final YogurtServer server;

    public MonitorTimeoutHandler(YogurtServer server) {
        this.server = server;
    }

    @Override
    public void handleTimeout() {
        log.info("Finished monitoring.");
        this.server.releaseServer();
    }
}
