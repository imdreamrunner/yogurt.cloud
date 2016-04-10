package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

public class MonitorHandler implements MessageHandler {
    private String path;

    public MonitorHandler(String path) {
        this.path = path;
    }

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        Logger.printRaw("File " + path + " is change. New data: ");
        if (receivingMessage.payload != null && receivingMessage.payload.length > 0) {
            Logger.printRaw(new String(receivingMessage.payload, SharedConfig.CONTENT_CHARSET));
        }
    }
}
