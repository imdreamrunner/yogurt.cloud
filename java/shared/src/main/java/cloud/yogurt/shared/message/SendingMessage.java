package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketSender;

import java.util.ArrayList;
import java.util.List;

/**
 * provides basic functionality for creating messages to send.
 *
 */
public abstract class SendingMessage {
    public abstract int getCallId();
    public abstract EndPoint getTarget();
    public abstract MessageDataLoader getMessageDataLoader();
}
