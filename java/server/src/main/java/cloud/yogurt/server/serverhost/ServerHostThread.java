package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.MessageServer;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.PacketHandler;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

public class ServerHostThread extends MessageServer {

    private ClientMessageHandler messageHandler;

    @Override
    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public ServerHostThread() {
        super(SERVER_PORT);
        messageHandler = new ClientMessageHandler();
    }
}
