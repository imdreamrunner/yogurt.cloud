package cloud.yogurt.shared.network;

import java.io.IOException;

public interface PacketHandler {

    void handlePacket(Packet packet) throws IOException, PacketException;

}
