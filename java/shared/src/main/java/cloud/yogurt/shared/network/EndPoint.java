package cloud.yogurt.shared.network;

import java.net.InetAddress;

public class EndPoint {
    public InetAddress address;
    public int port;

    @Override
    public String toString() {
        return address.toString() + ":" + port;
    }
}
