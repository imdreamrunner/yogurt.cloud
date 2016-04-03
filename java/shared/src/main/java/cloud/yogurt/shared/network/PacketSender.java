package cloud.yogurt.shared.network;

import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketException;

/**
 * Interface for anything that can send packets.
 *
 * The implementation of such sender should maintain a queue itself, if the
 * `sendPacket` action is not synchronous.
 */
public interface PacketSender {
    void sendPacket(Packet packet) throws PacketException;
}
