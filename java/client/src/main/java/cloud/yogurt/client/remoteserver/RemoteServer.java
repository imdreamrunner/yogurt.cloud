package cloud.yogurt.client.remoteserver;

import java.net.InetAddress;

public class RemoteServer {
    public InetAddress serverAddress;
    public int port;

    public RemoteServer(InetAddress address, int port) {
        this.serverAddress = address;
        this.port = port;
    }

}
