package cloud.yogurt.server.cmd;

import cloud.yogurt.server.serverhost.ServerHost;

import java.io.IOException;

public class YogurtServer {
    public static void main(String[] args) throws IOException {

        ServerHost host = ServerHost.getInstance();
        host.run();

        System.out.println("Press any key to stop.");
        int ignored = System.in.read();

        host.stop();
    }
}
