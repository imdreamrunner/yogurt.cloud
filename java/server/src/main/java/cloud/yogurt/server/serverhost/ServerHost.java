package cloud.yogurt.server.serverhost;

import java.io.IOException;

public class ServerHost {

    private static ServerHost instance;

    private ServerHostPacketHandler packetHandler;
    private ServerHostThread hostThread;

    public static void main(String[] args) throws IOException {
        instance = new ServerHost();
        instance.run();

        System.out.println("Press any key to stop.");
        System.in.read();

        instance.stop();
    }

    private void run() {
        System.out.println("Server is being started.");
        packetHandler = new ServerHostPacketHandler();
        hostThread = new ServerHostThread();
        hostThread.packetHandler = packetHandler;
        hostThread.start();
    }

    private void stop() {
        System.out.println("Server is being stopped.");
        hostThread.stopServerHost();
    }
}
