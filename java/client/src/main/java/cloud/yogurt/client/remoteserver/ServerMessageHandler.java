package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

public class ServerMessageHandler implements MessageHandler {
    private static Logger log = Logger.getLogger(ServerMessageHandler.class.getName());

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.debug("Receive message from server.");
        log.debug("Message header:\n" + receivingMessage.header.toString());

        String result = new String(receivingMessage.payload, SharedConfig.CONTENT_CHARSET);
        System.out.println("Server Response:\n" + result);
    }
}
