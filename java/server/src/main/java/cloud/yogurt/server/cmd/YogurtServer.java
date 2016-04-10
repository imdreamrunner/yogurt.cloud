package cloud.yogurt.server.cmd;

import cloud.yogurt.server.serverhost.ServerHost;
import cloud.yogurt.shared.logging.Logger;

import java.io.IOException;

/**
 * Command interface for Yogurt Server application.
 */
public class YogurtServer {

    public static void main(String[] args) throws IOException {
        ServerHost host = ServerHost.getInstance();
        host.run();

        Logger.printRaw("Press any key to stop.");
        int ignored = System.in.read();

        host.stop();
        Logger.printRaw("Bye.");
    }
}
