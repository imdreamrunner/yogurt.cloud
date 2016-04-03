package cloud.yogurt.shared.message;

import java.net.InetAddress;

/**
 * To whom the message will be sent to.
 */
public class MessageTarget {
    public InetAddress address;
    public int port;
}
