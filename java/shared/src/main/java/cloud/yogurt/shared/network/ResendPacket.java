package cloud.yogurt.shared.network;

import cloud.yogurt.shared.time.TimeoutHandler;

public class ResendPacket implements TimeoutHandler {
    private Packet packet;
    private DatagramServer datagramServer;

    ResendPacket(Packet packet, DatagramServer datagramServer) {
        this.packet = packet;
        this.datagramServer = datagramServer;
    }

    @Override
    public void handleTimeout() {
        if (!this.datagramServer.isPacketReceived(this.packet)) {
            try {
                this.datagramServer.sendPacket(this.packet);
            } catch (PacketException e) {
                e.printStackTrace();
            }
        }
    }
}
