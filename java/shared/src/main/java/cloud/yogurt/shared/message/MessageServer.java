package cloud.yogurt.shared.message;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.network.PacketHandler;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class MessageServer extends DatagramServer {
    private MessagePacketHandler messagePacketHandler = null;
    private static Logger log = Logger.getLogger(MessageServer.class.getName());

    public abstract MessageHandler getMessageHandler();

    public MessageServer() {
        super();
    }

    public MessageServer(int port) {
        super(port);
    }

    @Override
    protected PacketHandler getPacketHandler() {
        if (messagePacketHandler == null) {
            messagePacketHandler = new MessagePacketHandler(this);
        }
        return messagePacketHandler;
    }

    public void sendMessage(SendingMessage message) throws IOException, PacketException {
        SendMessageThread sendMessageThread = new SendMessageThread(message, this);
        sendMessageThread.start();
    }

}
