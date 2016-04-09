package cloud.yogurt.shared.sharedconfig;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class SharedConfig {
    public static String CURRENT_API_VERSION = "YC1";
    public static Charset HEADER_CHARSET = StandardCharsets.UTF_16;
    public static Charset CONTENT_CHARSET = StandardCharsets.US_ASCII;

    public static InetAddress SERVER_ADDRESS;
    public static int SERVER_PORT = 3000;
    public static int MAX_DATAGRAM = 100;  // byte, default 65507
    public static int PACKET_HEADER_SIZE = 4 * 3;
    public static int MAX_PACKET_PAYLOAD = MAX_DATAGRAM - PACKET_HEADER_SIZE;

    public static String CLIENT_BASE_PATH = System.getProperty("user.home") + "/YogurtCloud";
    public static String SERVER_BASE_PATH = System.getProperty("user.home") + "/YogurtServer";

    public static long RESENT_TIMEOUT = 1000;
    public static int MAXIMUM_RETRY = 10;

    static {
        try {
            SERVER_ADDRESS = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
