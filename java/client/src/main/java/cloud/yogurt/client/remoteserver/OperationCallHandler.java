package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;

public class OperationCallHandler implements MessageHandler {
    private YogurtServer server;

    OperationCallHandler(YogurtServer server) {
        this.server = server;
    }

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        Logger.printRaw("Operation finished.");
        this.server.releaseServer();
    }
}
