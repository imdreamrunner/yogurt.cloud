package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.Packet;

public interface SendingPacketHandler {
    void sendPacket(SendingPacket messagePacket);
    void removePacketIfNotSent(Packet packet);
}
