package cloud.yogurt.shared.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

        assertEquals(packet.id, decodedPacket.id);
        assertEquals(packet.ackPacket, decodedPacket.ackPacket);

        assertEquals(packet.content.length, decodedPacket.content.length);
        assertArrayEquals(packet.content, decodedPacket.content);

        System.out.println("All tests passed.\n\n");
    }

    @Test
    public void testPacketConstruct1() {
        packet.callId = 5;
        packet.eomFlag = true;
        packet.resFlag = false;
        packet.ackFlag = false;

        packet.id = 123;
        packet.ackPacket = 456;

        packet.content = new byte[]{1, 2, 3, 4, 5};
    }

    @Test
    public void testPacketConstruct2() {
        packet.callId = 7;
        packet.eomFlag = false;
        packet.resFlag = true;
        packet.ackFlag = false;

        packet.id = 1234;
        packet.ackPacket = 5678;

        packet.content = new byte[]{1, 2, 3, 4, 5, 6};
    }

    @Test
    public void testPacketConstruct3() {
        packet.callId = 9;
        packet.eomFlag = false;
        packet.resFlag = false;
        packet.ackFlag = true;

        packet.id = 0;
        packet.ackPacket = 1;

        packet.content = new byte[]{1, 2, 3, 4, 5, 7};
    }
}
