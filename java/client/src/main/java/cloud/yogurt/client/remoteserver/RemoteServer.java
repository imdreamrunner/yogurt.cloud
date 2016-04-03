package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.message.MessageServer;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.PacketHandler;

import java.net.InetAddress;

public class RemoteServer extends EndPoint {
    private DatagramServer datagramServer;

    public RemoteServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.datagramServer = new MessageServer() {        };
    }
}
