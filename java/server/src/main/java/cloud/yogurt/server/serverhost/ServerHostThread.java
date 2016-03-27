package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.PacketHandler;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

public class ServerHostThread extends DatagramServer {

    PacketHandler packetHandler;

    @Override
    protected PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public ServerHostThread() {
        super(SERVER_PORT);
    }
}
