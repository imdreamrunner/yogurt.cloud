package cloud.yogurt.shared.network;

import java.util.PriorityQueue;

public class ReceivingBuffer {
    private PriorityQueue<Packet> packets = new PriorityQueue<>((left, right) -> {
        if (left.id > right.id) return 1;
        else if (left.id == right.id) return 0;
        else return -1;
    });

    public void add(Packet packet) {
        packets.add(packet);
    }

    public Packet peek() {
        return packets.peek();
    }

    public Packet poll() {
        return packets.poll();
    }
}
