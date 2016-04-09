package cloud.yogurt.shared.network;

import java.util.HashMap;
import java.util.Map;

public class SendBuffer {
    private Map<Long, Packet> bufferedPackets = new HashMap<>();

    public void bufferPacket(Packet packet) {
        bufferedPackets.put(packet.id, packet);
    }
}
