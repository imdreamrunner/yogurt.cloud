package cloud.yogurt.shared.sharedconfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SharedConfig {
    public static String CURRENT_API_VERSION = "YC1";
    public static Charset HEADER_CHARSET = StandardCharsets.UTF_16;

    public static InetAddress SERVER_ADDRESS;
    public static int SERVER_PORT = 3000;
    public static int MAX_DATAGRAM = 65527;  // byte, default 65527

    public static String CLIENT_BASE_PATH = System.getProperty("user.home") + "/YogurtCloud";
    public static String SERVER_BASE_PATH = System.getProperty("user.home") + "/YogurtServer";

    {
        try {
            SERVER_ADDRESS = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
