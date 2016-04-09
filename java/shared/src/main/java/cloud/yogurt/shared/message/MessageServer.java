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

/**
 * Message server that acts as a wrapper of UDP servers
 */
public abstract class MessageServer extends DatagramServer implements MessageSender {
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
    /**
     * Create thread and send message
     * @param message message to send
     */
    public void sendMessage(SendingMessage message) {
        SendMessageThread sendMessageThread = new SendMessageThread(message, this);
        sendMessageThread.start();
    }

}
