package cloud.yogurt.server.cmd;

import cloud.yogurt.server.serverhost.ServerHost;

import java.io.IOException;

public class YogurtServer {
    private static ServerHost host;

    public static void main(String[] args) throws IOException {

        host = ServerHost.getInstance();
        host.run();

        System.out.println("Press any key to stop.");
        int ignored = System.in.read();

        host.stop();
        System.out.println("Bye.");
    }
}
