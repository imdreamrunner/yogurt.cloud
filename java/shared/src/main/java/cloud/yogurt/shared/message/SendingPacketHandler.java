package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.Packet;

/**
 * Interface for anything that can send packets.
 *
 * The implementation of such sender should maintain a queue itself, if the
 * `sendPacket` action is not synchronous.
 */
public interface SendingPacketHandler {
    void sendPacket(SendingPacket messagePacket);
    void removePacketIfNotSent(Packet packet);
}
