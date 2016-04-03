package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * provides basic functionality for creating messages to send.
 *
 */
public abstract class SendingMessage {
    private List<Packet> packetsBeingSent = new ArrayList<>();

    public abstract int getCallId();
    public abstract EndPoint getMessageTarget();
    protected abstract PacketSender getPacketSender();
    protected abstract MessageDataLoader getMessageDataLoader();

    private MessageDataLoader loader = getMessageDataLoader();
    private PacketSender sender = getPacketSender();

    public void send() throws IOException, PacketException {
        while (loader.available() > 0) {
            Packet packet = new Packet();
            packet.callId = getCallId();
            packet.endPoint = getMessageTarget();
            int packetSize = Math.min(SharedConfig.MAX_DATAGRAM, loader.available());
            packet.content = new byte[loader.available()];
            int bytesRead = loader.read(packet.content);
            if (bytesRead != packetSize) {
                throw new PacketException("Bytes read is not equal to packet size.");
            }
            if (loader.available() == 0) {
                packet.eomFlag = true;
            }
            sender.sendPacket(packet);
        }
    }
}
