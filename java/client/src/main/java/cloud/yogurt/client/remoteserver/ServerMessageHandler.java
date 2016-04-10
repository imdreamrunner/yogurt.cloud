package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle the message from server.
 */
public class ServerMessageHandler implements MessageHandler {
    private static Logger log = Logger.getLogger(ServerMessageHandler.class.getName());

    private Map<Integer, MessageHandler> handlers = new HashMap<>();

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.debug("Receive message from server.");
        log.debug("Message header:\n" + receivingMessage.header.toString());

        String result = new String(receivingMessage.payload, SharedConfig.CONTENT_CHARSET);
        log.debug("Server Response:\n" + result);

        if (handlers.get(receivingMessage.callId) != null) {
            handlers.get(receivingMessage.callId).handleMessage(receivingMessage);
        }
    }

    public void registerHandler(int callId, MessageHandler serverMessageHandler) {
        handlers.put(callId, serverMessageHandler);
    }
}
