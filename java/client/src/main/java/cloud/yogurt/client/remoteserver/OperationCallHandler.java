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
        if (receivingMessage.header.getParams()[1].equals("SUCCESS")) {
            Logger.printRaw("Operation succeed.");
        } else {
            Logger.printRaw("Operation failed.");
        }
        this.server.releaseServer();
    }
}
