package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.logging.Logger;

public class ServerHost {

    private static Logger log = Logger.getLogger(ServerHost.class.getName());

    private static ServerHost instance;

    private ServerHostPacketHandler packetHandler;
    private ServerHostThread hostThread;

    public static ServerHost getInstance() {
        if (instance == null) {
            instance = new ServerHost();
        }
        return instance;
    }

    public void run() {
        log.info("Server is being started.");
        packetHandler = new ServerHostPacketHandler();
        hostThread = new ServerHostThread();
        hostThread.packetHandler = packetHandler;
        hostThread.start();
    }

    public void stop() {
        log.info("Server is being stopped.");
        hostThread.stopServer();
    }
}
