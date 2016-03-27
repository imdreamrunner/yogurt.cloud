package cloud.yogurt.shared.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

public class Packet {
    public int id;
    public int ackPacket;

    public int callId;
    public boolean ackFlag;
    public boolean resFlag;
    public boolean eomFlag;

    public byte[] content;

    public byte[] construct() throws PacketException {
        int PACKET_HEADER_SIZE = 32 * 3;
        if (content == null) content = new byte[0];
        if (PACKET_HEADER_SIZE + content.length > MAX_DATAGRAM) {
            throw new PacketException("Packet size too large");
        }

        byte[] datagram = new byte[PACKET_HEADER_SIZE + content.length];
        System.arraycopy(getByteFromInteger(callId, 2), 0, datagram, 0, 2);

        boolean[] bitFlags = new boolean[16];
        bitFlags[0] = resFlag;
        bitFlags[1] = ackFlag;
        bitFlags[2] = eomFlag;
        int bitFlagsInts = constructIntegerWithFlags(bitFlags);
        System.arraycopy(getByteFromInteger(bitFlagsInts, 2), 0, datagram, 2, 2);

        return datagram;
    }

    private int constructIntegerWithFlags(boolean[] flags) {
        int result = 0;
        for (int i = 0; i < flags.length; i++) {
            result <<= 1;
            result += flags[i] ? 1 : 0;
        }
        return result;
    }

    private boolean[] getBitFromInteger(int number, int length) {
        boolean[] flags = new boolean[length];
        for (int i = length - 1; i >= 0; i--) {
            flags[i] = number % 2 == 1;
            number /= 2;
        }
        return flags;
    }

    private byte[] getByteFromInteger(int number, int length) throws PacketException {
        if (number > 1 << (length * 8)) {
            throw new PacketException("Number out of bound");
        }
        byte[] result = new byte[length];
        for (int i = length - 1; i >= 0; i--) {
            result[i] = (byte) (number % (1 << 8));
            number /= 1 << 8;
        }
        return result;
    }

    public void decodeDatagram(byte[] bytes) {
        this.callId = readIntegerFromByte(bytes, 0, 2);
        int flagInteger = readIntegerFromByte(bytes, 2, 2);
        boolean[] flags = getBitFromInteger(flagInteger, 16);
        this.resFlag = flags[0];
        this.ackFlag = flags[1];
        this.eomFlag = flags[2];
    }

    private int readIntegerFromByte(byte[] bytes, int start, int length) {
        int result = 0;
        for (int i = 0; i < length; i++) {
            result <<= 8;
            result += (int) bytes[i + start] & 0xff;  // Convert to an unsigned byte.
        }
        return result;
    }

}
