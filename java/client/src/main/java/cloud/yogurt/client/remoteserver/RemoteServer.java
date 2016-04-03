package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.servicecall.ServiceCall;
import cloud.yogurt.shared.message.MessageServer;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.PacketException;

import java.io.IOException;
import java.net.InetAddress;

public class RemoteServer extends EndPoint {
    private MessageServer messageServer;

    public RemoteServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;
        this.messageServer = new MessageServer();
        this.messageServer.start();
    }

    public void makeServiceCall(ServiceCall serviceCall) throws IOException, PacketException {
        serviceCall.setTarget(this);
        this.messageServer.sendMessage(serviceCall);
    }

    public void stop() {
        this.messageServer.stopServer();
    }
}
