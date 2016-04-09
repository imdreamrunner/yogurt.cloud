package cloud.yogurt.shared.network;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static cloud.yogurt.shared.sharedconfig.SharedConfig.*;

/**
 * Packet class
 *  contains EndPoint indicator
 *  Packet ID, Ack Pack ID;
 *  Call ID, Flags
 *  
 *  Lower layer transmission carrier.
 */
public class Packet {

    public EndPoint endPoint;

    public long id = -1;  // 4 bytes
    public long ackPacket;  // 4 bytes

    public int callId;  // 2 bytes
    public boolean ackFlag;
    public boolean resFlag;
    public boolean eomFlag;

    public byte[] content;

    /**
     * Packet constuction function
     *  takes packet header and content
     * @return datagram packet
     * @throws PacketException
     */
    public byte[] construct() throws PacketException {
        if (content == null) content = new byte[0];
        if (content.length > MAX_PACKET_PAYLOAD) {
            throw new PacketException("Packet payload size too large, actual = " + content.length);
        }

        byte[] datagram = new byte[PACKET_HEADER_SIZE + content.length];
        System.arraycopy(getByteFromInteger(callId, 2), 0, datagram, 0, 2);

        boolean[] bitFlags = new boolean[16];
        bitFlags[0] = resFlag;
        bitFlags[1] = ackFlag;
        bitFlags[2] = eomFlag;
        int bitFlagsInts = constructIntegerWithFlags(bitFlags);
        System.arraycopy(getByteFromInteger(bitFlagsInts, 2), 0, datagram, 2, 2);

        System.arraycopy(getByteFromInteger(id, 4), 0, datagram, 4, 4);
        System.arraycopy(getByteFromInteger(ackPacket, 4), 0, datagram, 8, 4);

        System.arraycopy(content, 0, datagram, 12, content.length);

        return datagram;
    }
    /**
     *
     * @param flags
     * @return
     */
    private int constructIntegerWithFlags(boolean[] flags) {
        int result = 0;
        for (boolean flag : flags) {
            result <<= 1;
            result += flag ? 1 : 0;
        }
        return result;
    }

    /**
     *
     * @param number
     * @param length
     * @return
     */
    private boolean[] getBitFromInteger(int number, int length) {
        boolean[] flags = new boolean[length];
        for (int i = length - 1; i >= 0; i--) {
            flags[i] = number % 2 == 1;
            number /= 2;
        }
        return flags;
    }

    /**
     *
     * @param number
     * @param length
     * @return
     * @throws PacketException
     */
    private byte[] getByteFromInteger(long number, int length) throws PacketException {
        if (number > ((long)1 << (length * 8))) {
            throw new PacketException("Number out of bound. Maximum " + (1 << (length * 8)) + " found " + number);
        }
        byte[] result = new byte[length];
        for (int i = length - 1; i >= 0; i--) {
            result[i] = (byte) (number % (1 << 8));
            number /= 1 << 8;
        }
        return result;
    }

    /**
     * Datagram decode function
     * 
     * @param bytes
     * 
     * return packet message to class abtributes
     */
    public void decodeDatagram(byte[] bytes) {
        this.callId = readIntegerFromByte(bytes, 0, 2);
        int flagInteger = readIntegerFromByte(bytes, 2, 2);
        boolean[] flags = getBitFromInteger(flagInteger, 16);
        this.resFlag = flags[0];
        this.ackFlag = flags[1];
        this.eomFlag = flags[2];

        this.id = readIntegerFromByte(bytes, 4, 4);
        this.ackPacket = readIntegerFromByte(bytes, 8, 4);

        int contentLength = bytes.length - PACKET_HEADER_SIZE;
        content = new byte[contentLength];
        System.arraycopy(bytes, 12, content, 0, contentLength);
    }

    /**
     * Decode flatten integer from bytes
     * @param bytes
     * @param start
     * @param length
     * @return integer value
     */
    private int readIntegerFromByte(byte[] bytes, int start, int length) {
        int result = 0;
        for (int i = 0; i < length; i++) {
            result <<= 8;
            result += (int) bytes[i + start] & 0xff;  // Convert to an unsigned byte.
        }
        return result;
    }

}
