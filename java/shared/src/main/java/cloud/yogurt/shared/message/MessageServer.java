package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.PacketHandler;

public class MessageServer extends DatagramServer {
    @Override
    protected PacketHandler getPacketHandler() {
        return null;
    }
}
