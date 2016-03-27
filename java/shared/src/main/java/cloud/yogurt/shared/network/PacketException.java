package cloud.yogurt.shared.network;

import cloud.yogurt.shared.common.YogurtException;

public class PacketException extends YogurtException {
    public PacketException(String s) {
        super("PacketException: " + s);
    }
}
