package cloud.yogurt.shared.sharedconfig;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SharedConfig {
    public static InetAddress SERVER_ADDRESS;
    public static int SERVER_PORT = 3000;
    public static int MAX_DATAGRAM = 65527;

    {
        try {
            SERVER_ADDRESS = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
