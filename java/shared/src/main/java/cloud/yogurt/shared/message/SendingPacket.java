package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.Packet;

/**
 * A packet holding enough information for send message handler to send message.
 */
public class SendingPacket {
    public SendingMessage message;
    public Packet packet;
}
