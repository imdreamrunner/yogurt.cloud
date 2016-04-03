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
        log.info("Send message to " + message.getTarget().address + ":" + message.getTarget().port + ".");
        MessageDataLoader loader = message.getMessageDataLoader();
        List<Packet> packets = new ArrayList<>();
        while (true) {
            Packet packet = new Packet();
            packet.callId = message.getCallId();
            packet.endPoint = message.getTarget();
            byte[] buffer = new byte[SharedConfig.MAX_PACKET_PAYLOAD];
            int bytesRead = loader.read(buffer);
            if (bytesRead <= 0) {
                break;
            } else {
                log.debug("Read " + bytesRead + " bytes from data loader.");
                packet.content = new byte[bytesRead];
                System.arraycopy(buffer, 0, packet.content, 0, bytesRead);
            }
            packets.add(packet);
        }
        packets.get(packets.size() - 1).eomFlag = true;
        for (Packet packet : packets) sendPacket(packet);
        log.debug("Finish sending message.");
    }

}
