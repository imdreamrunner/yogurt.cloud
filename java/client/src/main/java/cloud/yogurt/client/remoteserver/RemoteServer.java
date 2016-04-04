package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.servicecall.ServiceCall;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.MessageServer;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.PacketException;

import java.io.IOException;
import java.net.InetAddress;

public class RemoteServer extends EndPoint {
    private MessageServer messageServer;
    private ServerMessageHandler messageHandler;

    public RemoteServer(InetAddress address, int port) {
        this.address = address;
        this.port = port;

        this.messageHandler = new ServerMessageHandler();

        this.messageServer = new MessageServer() {
            @Override
            public MessageHandler getMessageHandler() {
                return messageHandler;
            }
        };
        this.messageServer.start();
    }

    public int makeServiceCall(ServiceCall serviceCall) throws IOException, PacketException {
        serviceCall.setTarget(this);
        this.messageServer.sendMessage(serviceCall);
        return serviceCall.getCallId();
    }

    public void stop() {
        this.messageServer.stopServer();
    }

    public ServerMessageHandler getMessageHandler() {
        return messageHandler;
    }
}
