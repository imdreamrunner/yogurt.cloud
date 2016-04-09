package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.network.PacketHandler;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessagePacketHandler implements PacketHandler {

    private static Logger log = Logger.getLogger(MessagePacketHandler.class.getName());

    private MessageServer server;

    List<Packet> receiveBuffer = new ArrayList<>();

    public MessagePacketHandler(MessageServer server) {
        this.server = server;
    }

    @Override
    public void handlePacket(Packet packet) {
        receiveBuffer.add(packet);
        if (packet.eomFlag) {
            consumePackets();
        }
    }

    /**
     * Consume an sequence of received packets.
     */
    synchronized private void consumePackets() {
        int dataSize = 0;
        for (Packet packet : receiveBuffer) {
            dataSize += packet.content.length;
        }
        byte[] data = new byte[dataSize];
        int pos = 0;

        for (Packet packet : receiveBuffer) {
            System.arraycopy(packet.content, 0, data, pos, packet.content.length);
            pos += packet.content.length;
        }

        log.info("Total message length: " + data.length);

        int payloadOffset = 0;
        boolean previousNewLine = false;
        StringBuilder headerString = new StringBuilder();

        for (int i = 0; i < data.length; i += 2) {
            byte[] nextChar = new byte[2];
            nextChar[0] = data[i];
            nextChar[1] = data[i+1];
            String partial = new String(nextChar, SharedConfig.HEADER_CHARSET);
            headerString.append(partial);
            if (partial.equals("\n")) {
                if (previousNewLine) {
                    payloadOffset = i + 2;
                    break;
                } else {
                    previousNewLine = true;
                }
            } else {
                previousNewLine = false;
            }
        }

        Header header = Header.fromString(headerString.toString());

        byte[] payload = new byte[dataSize - payloadOffset];
        System.arraycopy(data, payloadOffset, payload, 0, payload.length);

        int callId = receiveBuffer.get(receiveBuffer.size() - 1).callId;
        EndPoint source = receiveBuffer.get(receiveBuffer.size() - 1).endPoint;

        ReceivingMessage message = new ReceivingMessage(callId, source, header, payload);
        server.getMessageHandler().handleMessage(message);

        receiveBuffer.clear();
    }
}
