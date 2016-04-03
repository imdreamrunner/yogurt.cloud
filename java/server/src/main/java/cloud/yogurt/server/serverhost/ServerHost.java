package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.logging.Logger;

public class ServerHost {

    private static Logger log = Logger.getLogger(ServerHost.class.getName());

    private static ServerHost instance;

    private ServerHostThread hostThread;

    public static ServerHost getInstance() {
        if (instance == null) {
            instance = new ServerHost();
        }
        return instance;
    }

    public void run() {
        log.info("Server is being started.");
        hostThread = new ServerHostThread();
        hostThread.start();
    }

    public void stop() {
        log.info("Server is being stopped.");
        hostThread.stopServer();
    }
}
