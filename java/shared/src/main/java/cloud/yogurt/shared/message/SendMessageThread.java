package cloud.yogurt.shared.message;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.DatagramServer;
import cloud.yogurt.shared.network.Packet;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Message sending thread class that is used to send message 
 * in a muli-threading mannaer
 */
public class SendMessageThread extends Thread {
    private SendingMessage message;
    private DatagramServer server;
    public SendMessageThread(SendingMessage message, DatagramServer server) {
        this.message = message;
        this.server = server;
    }

    private static Logger log = Logger.getLogger(SendMessageThread.class.getName());

    /**
     * Send message execution
     * Chunk message if exceeds MAX_PACKET_PAYLOAD
     */
    public void run() {
        try {
            log.info("Send message to " + message.getTarget().address + ":" + message.getTarget().port + ".");
            MessageDataLoader loader = message.getMessageDataLoader();
            List<Packet> packets = new ArrayList<>();
            while (true) {
                Packet packet = new Packet();
                packet.callId = message.getCallId();
                packet.endPoint = message.getTarget();
                byte[] buffer = new byte[SharedConfig.MAX_PACKET_PAYLOAD];
                int bytesRead = loader.read(buffer);
                if (bytesRead <= 0) {
                    break;
                } else {
                    log.debug("Read " + bytesRead + " bytes from data loader.");
                    packet.content = new byte[bytesRead];
                    System.arraycopy(buffer, 0, packet.content, 0, bytesRead);
                }
                packets.add(packet);
            }
            packets.get(packets.size() - 1).eomFlag = true;
            for (Packet packet : packets) server.sendPacket(packet);
            log.debug("Finish sending message.");
        } catch (PacketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
