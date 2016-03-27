package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * provides basic functionality for creating messages.
 *
 */
public abstract class SendingMessage {
    private Queue<byte[]> data = new LinkedBlockingQueue<>();
    private int byteSent;
    private boolean dataFinished = false;

    private List<Packet> packetsBeingSent = new ArrayList<>();

    public abstract MessageTarget getMessageTarget();
    protected abstract SendingPacketHandler getMessagePacketHandler();
    protected abstract MessageLoadDataHandler getMessageLoadDataHandler();

    public void appendData(byte[] bytes) {
        this.data.add(bytes);
    }

    public void finishData() {
        dataFinished = true;
    }

    public void handlePacketSent(Packet packet) {

    }
}
