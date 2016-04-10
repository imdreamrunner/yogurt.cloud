package cloud.yogurt.shared.network;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.time.SetTimeout;

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

    /**
     * Next packet ID to be used for an endpoint call.
     */
    private Map<EndPointCall, Long> sendId = new HashMap<>();

    /**
     * As a receiver, next packet ID to be received for and endpoint call.
     */
    private Map<EndPointCall, Long> ackId = new HashMap<>();

    /**
     * As a sender, next packet ID to be received by receiver.
     */
    private Map<EndPointCall, Long> receivedId = new HashMap<>();

    /**
     * Receive buffer.
     */
    private Map<EndPointCall, ReceivingBuffer> receivingBuffers = new HashMap<>();

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

    /**
     * Server waits for user datagram
     *  with message control
     */
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

            log.debug("Receive packet " + packet.toString());

            EndPointCall endPointCall = new EndPointCall(packet);

            setReceivedId(endPointCall, packet.ackPacket);


            if (getAckId(endPointCall) == packet.id) {
                // The packet is exactly what we want.
                setAckId(endPointCall, packet.id);
                getPacketHandler().handlePacket(packet);
                if (!packet.ackFlag) {
                    sendAck(packet.endPoint, packet.callId);
                }

                ReceivingBuffer receivingBuffer = receivingBuffers.get(endPointCall);

                if (receivingBuffer != null) {
                    while(receivingBuffer.peek() != null) {
                        Packet bufferedPacket = receivingBuffer.peek();
                        if (getAckId(endPointCall) == bufferedPacket.id) {
                            getPacketHandler().handlePacket(bufferedPacket);
                            setAckId(endPointCall, bufferedPacket.id);
                            receivingBuffer.poll();
                        } else if (getAckId(endPointCall) > bufferedPacket.id) {
                            // The packet is useless.
                            receivingBuffer.poll();
                        } else {
                            // The packet is for the future.
                            break;
                        }
                    }
                }

            } else if (getAckId(endPointCall) < packet.id) {
                // The packet is for the future.
                if (receivingBuffers.get(endPointCall) == null) {
                    receivingBuffers.put(endPointCall, new ReceivingBuffer());
                }
                ReceivingBuffer receivingBuffer = receivingBuffers.get(endPointCall);
                receivingBuffer.add(packet);
            } else {
                log.debug("Packet is dropped, since it's been received.");
                sendAck(packet.endPoint, packet.callId);
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

    private long getAckId(EndPointCall endPointCall ) {
        if (ackId.get(endPointCall) != null) {
            return ackId.get(endPointCall);
        }
        return 0;
    }

    /**
     * Store accepted packets from sender locally.
     * @return true only if is receiving next frame.
     */
    private void setAckId(EndPointCall endPointCall, long id) {
        if (getAckId(endPointCall) == id) {
            ackId.put(endPointCall, id + 1);
        }
    }

    /**
     * Simple packet with ACK included
     * @param endPoint to whom the packet is sent to.
     * @param callId call ID of the packet.
     * @throws PacketException
     */
    private void sendAck(EndPoint endPoint, int callId) throws PacketException {
        Packet packet = new Packet();
        packet.callId = callId;
        packet.endPoint = endPoint;
        packet.ackFlag = true;
        sendPacket(packet);
    }

    private long getReceivedId(EndPointCall endPointCall) {
        Long receivedId = this.receivedId.get(endPointCall);
        if (receivedId == null) {
            return 0;
        } else {
            return receivedId;
        }
    }

    private void setReceivedId(EndPointCall endPointCall, long id) {
        if (getReceivedId(endPointCall) < id) {
            log.debug("SET_RECEIVED_ID", "Packet " + id + " i from " + endPointCall);
            this.receivedId.put(endPointCall, id);
        }
    }

    public boolean isPacketReceived(Packet packet) {
        if (getReceivedId(new EndPointCall(packet)) > packet.id) {
            log.debug("CHECK_PACKET_RECEIVED", packet.toString() + " is received.");
            return true;
        }
//        log.debug("CHECK_PACKET_RECEIVED", packet.toString() + " is not received.");
        return false;
    }


    public boolean isNextToSend(Packet packet) {
        return getReceivedId(new EndPointCall(packet)) == packet.id;
    }

    /**
     * Send packet
     *  set packet id
     *      ack packet id
     *  construct packet
     *      log message transmission
     * use datagram socket
     * @param packet reference for packet construction
     * @throws PacketException
     */
    public void sendPacket(Packet packet, int retry) throws PacketException {
        if (packet.id < 0) {
            if (packet.ackFlag) {
                // We set packet ID to 0 for pure ACK packets.
                // We don't care if they are lost.
                packet.id = 0;
            } else {
                packet.id = getSendId(packet.endPoint, packet.callId);
            }
        }
        packet.ackPacket = getAckId(new EndPointCall(packet.endPoint, packet.callId));
        byte[] constructedPacket = packet.construct();
        DatagramPacket sendingDatagram = new DatagramPacket(constructedPacket, constructedPacket.length);
        sendingDatagram.setAddress(packet.endPoint.address);
        sendingDatagram.setPort(packet.endPoint.port);
        if (!packet.ackFlag) {
            SetTimeout.setTimeout(new ResendPacket(packet, this, retry), RESENT_TIMEOUT);
        }
        try {
            log.info("Send packet " + packet.toString());
            socket.send(sendingDatagram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendPacket(Packet packet) throws PacketException {
        sendPacket(packet, 0);
    }

}
