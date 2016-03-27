package cloud.yogurt.shared.network;

import cloud.yogurt.shared.message.SendingPacket;
import cloud.yogurt.shared.message.SendingPacketHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

public abstract class DatagramServer extends Thread implements SendingPacketHandler {

    private boolean isStopped = false;

    private int port;

    private DatagramSocket socket;

    private byte[] receiveBuffer = new byte[MAX_DATAGRAM];
    private DatagramPacket receiveDatagram = new DatagramPacket(receiveBuffer, MAX_DATAGRAM);

    private List<SendingPacket> packetsToSend = new LinkedList<>();

    protected abstract PacketHandler getPacketHandler();

    public DatagramServer() {
        this.port = 0;
    }

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
        System.out.println("Socket server is listening at port " + socket.getLocalPort());
        while (!isStopped) {
            waitForDatagram();
        }
    }

    public void stopServerHost() {
        isStopped = true;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    private void waitForDatagram() {
        System.out.println("Waiting for incoming datagram.");

        try {
            socket.receive(receiveDatagram);
            Packet packet = new Packet();
            packet.decodeDatagram(receiveDatagram.getData());
            getPacketHandler().handlePacket(packet);
        }
        catch (SocketException sc) {
            System.out.println("Socket server is closed.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPacket(SendingPacket messagePacket) {
        packetsToSend.add(messagePacket);
    }

    private void doSendPacket(SendingPacket messagePacket) throws PacketException {
        byte[] constructedPacket = messagePacket.packet.construct();
        DatagramPacket sendingDatagram = new DatagramPacket(constructedPacket, constructedPacket.length);
        sendingDatagram.setAddress(messagePacket.message.getMessageTarget().address);
        sendingDatagram.setPort(messagePacket.message.getMessageTarget().port);
        try {
            socket.send(sendingDatagram);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messagePacket.message.handlePacketSent(messagePacket.packet);
    }

    @Override
    public void removePacketIfNotSent(Packet packet) {

    }

}
