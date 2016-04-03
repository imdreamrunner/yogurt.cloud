package cloud.yogurt.shared.network;

import cloud.yogurt.shared.logging.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

/**
 * The default sender and receiver for Yogurt packets.
 *
 * Currently the implementation for `sendPacket` is asynchronized, i.e.
 * packet may not be sent after function returns.
 */
public abstract class DatagramServer extends Thread implements PacketSender {

    private static Logger log = Logger.getLogger(DatagramServer.class.getName());

    private boolean isStopped = false;

    private int port;

    private DatagramSocket socket;

    private byte[] receiveBuffer = new byte[MAX_DATAGRAM];
    private DatagramPacket receiveDatagram = new DatagramPacket(receiveBuffer, MAX_DATAGRAM);

    private List<Packet> packetsToSend = new LinkedList<>();

    private Map<EndPointCall, Long> sendId = new HashMap<>();
    private Map<EndPointCall, Long> ackId = new HashMap<>();

    protected abstract PacketHandler getPacketHandler();

    /**
     * Default constructor without specific port.
     *
     * This constructor is typical used by client, since it does not need to listen on a specific port.
     */
    public DatagramServer() {
        this.port = 0;
    }

    /**
     * The constructor with a specific port.
     *
     * @param port Which port would the server be listening on.
     */
    public DatagramServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            if (this.port != 0) {
                socket = new DatagramSocket(port);
            } else {
                // Create socket in any port.
                socket = new DatagramSocket();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        log.info("Socket server is listening at port " + socket.getLocalPort());
        while (!isStopped) {
            waitForDatagram();
        }
    }

    public void stopServer() {
        log.debug("Stopping datagram server on port " + this.socket.getLocalPort() + ".");
        isStopped = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private void waitForDatagram() {
        log.debug("Waiting for incoming datagram.");

        try {
            socket.receive(receiveDatagram);
            byte[] buffer = new byte[receiveDatagram.getLength()];
            System.arraycopy(receiveDatagram.getData(), 0, buffer, 0, buffer.length);
            Packet packet = new Packet();
            packet.decodeDatagram(buffer);
            packet.endPoint = new EndPoint();
            packet.endPoint.address = receiveDatagram.getAddress();
            packet.endPoint.port = receiveDatagram.getPort();

            log.debug("Receive datagram size of " + buffer.length + " from " +
                    packet.endPoint.address + ":" + packet.endPoint.port + "," +
                    " Call=" + packet.callId +
                    " Packet=" + packet.id +
                    " ACK=" + packet.ackPacket);

            if (setAckId(packet.endPoint, packet.callId, packet.id)) {
                log.debug("Package is accepted.");
                getPacketHandler().handlePacket(packet);
                if (!packet.ackFlag) {
                    sendAck(packet.endPoint, packet.callId);
                }
            } else {
                log.debug("Package is dropped.");
            }

        }
        catch (SocketException sc) {
            log.error("Socket server is closed.");
        }
        catch (IOException | PacketException e) {
            e.printStackTrace();
        }

    }

    private long getSendId(EndPoint endPoint, int call) {
        EndPointCall endPointCall = new EndPointCall(endPoint, call);
        if (sendId.get(endPointCall) != null) {
            sendId.put(endPointCall, sendId.get(endPointCall) + 1);
        }
        else {
            sendId.put(endPointCall, 1l);
        }
        return sendId.get(endPointCall) - 1;
    }

    private long getAckId(EndPoint endPoint, int call ) {
        EndPointCall endPointCall = new EndPointCall(endPoint, call);
        if (ackId.get(endPointCall) != null) {
            return ackId.get(endPointCall);
        }
        return 0;
    }

    /**
     * Set ACK
     * @param endPoint
     * @param call
     * @param id
     * @return true only if is receiving next frame.
     */
    private boolean setAckId(EndPoint endPoint, int call, long id) {
        if (getAckId(endPoint, call) == id) {
            EndPointCall endPointCall = new EndPointCall(endPoint, call);
            ackId.put(endPointCall, id + 1);
            return true;
        }
        return false;
    }

    private void sendAck(EndPoint endPoint, int call) throws PacketException {
        Packet packet = new Packet();
        packet.callId = call;
        packet.endPoint = endPoint;
        packet.ackFlag = true;
        sendPacket(packet);
    }

    public void sendPacket(Packet packet) throws PacketException {
        packet.id = getSendId(packet.endPoint, packet.callId);
        packet.ackPacket = getAckId(packet.endPoint, packet.callId);
        byte[] constructedPacket = packet.construct();
        log.info("Send packet size " + constructedPacket.length + " to " +
                packet.endPoint.address + ":" + packet.endPoint.port + "," +
                " Call=" + packet.callId +
                " Packet=" + packet.id +
                " ACK=" + packet.ackPacket);
        DatagramPacket sendingDatagram = new DatagramPacket(constructedPacket, constructedPacket.length);
        sendingDatagram.setAddress(packet.endPoint.address);
        sendingDatagram.setPort(packet.endPoint.port);
        try {
            socket.send(sendingDatagram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
