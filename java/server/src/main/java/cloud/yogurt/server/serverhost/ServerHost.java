package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.logging.Logger;

import java.io.IOException;

public class ServerHost {

    private static Logger log = Logger.getLogger(ServerHost.class.getName());

    private static ServerHost instance;

    private ServerHostPacketHandler packetHandler;
    private ServerHostThread hostThread;

    public static void main(String[] args) throws IOException {
        instance = new ServerHost();
        instance.run();

        log.info("Press any key to stop.");
        System.in.read();

        instance.stop();
    }

    private void run() {
        log.info("Server is being started.");
        packetHandler = new ServerHostPacketHandler();
        hostThread = new ServerHostThread();
        hostThread.packetHandler = packetHandler;
        hostThread.start();
    }

    private void stop() {
        log.info("Server is being stopped.");
        hostThread.stopServerHost();
    }
}
