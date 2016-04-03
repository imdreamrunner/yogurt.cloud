package cloud.yogurt.shared.network;

import cloud.yogurt.shared.logging.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
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
            Packet packet = new Packet();
            packet.decodeDatagram(receiveDatagram.getData());
            getPacketHandler().handlePacket(packet);
        }
        catch (SocketException sc) {
            log.error("Socket server is closed.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPacket(Packet packet) throws PacketException {
        byte[] constructedPacket = packet.construct();
        log.info("Send packet size " + constructedPacket.length + " to " +
                packet.endPoint.address + ":" + packet.endPoint.port + ".");
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
