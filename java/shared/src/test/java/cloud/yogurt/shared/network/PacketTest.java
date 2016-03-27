package cloud.yogurt.shared.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.annotation.XmlAttribute;

import static org.junit.Assert.*;

public class PacketTest {

    private Packet packet;

    @Before
    public void preparePacket() {
        System.out.println("Create empty packet for testing.");
        packet = new Packet();
    }

    @After
    public void testPacket() throws PacketException {
        Packet decodedPacket = new Packet();
        decodedPacket.decodeDatagram(packet.construct());

        System.out.println("Compare constructed packet with original one.");

        assertEquals(packet.callId, decodedPacket.callId);
        assertEquals(packet.eomFlag, decodedPacket.eomFlag);
        assertEquals(packet.resFlag, decodedPacket.resFlag);
        assertEquals(packet.ackFlag, decodedPacket.ackFlag);

        System.out.println("All tests passed.\n\n");
    }

    @Test
    public void testPacketConstruct1() {
        packet.callId = 5;
        packet.eomFlag = true;
        packet.resFlag = false;
        packet.ackFlag = false;
    }

    @Test
    public void testPacketConstruct2() {
        packet.callId = 7;
        packet.eomFlag = false;
        packet.resFlag = true;
        packet.ackFlag = false;
    }

    @Test
    public void testPacketConstruct3() {
        packet.callId = 7;
        packet.eomFlag = false;
        packet.resFlag = false;
        packet.ackFlag = true;
    }
}
