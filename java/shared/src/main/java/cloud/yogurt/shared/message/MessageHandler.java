package cloud.yogurt.shared.message;

import cloud.yogurt.shared.network.PacketException;

import java.io.IOException;

public interface MessageHandler {
    void handleMessage(ReceivingMessage receivingMessage) throws IOException, PacketException;
}
