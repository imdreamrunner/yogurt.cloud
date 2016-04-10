package cloud.yogurt.client.remoteserver;

import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;

public class CheckStatusHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    YogurtServer server;

    public CheckStatusHandler(String filename, YogurtServer server) {
        this.filename = filename;
        this.server = server;
    }


    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.info("Handle file status, and check cache validation.");
        if (receivingMessage.header.getValue("LastModify") != null) {
            Logger.printRaw("Last update time for file " + filename + " is " +
                    ((HeaderIntegerValue) receivingMessage.header.getValue("LastModify")).getValue());
        } else {
            Logger.printRaw("File does not exist.");
        }
        this.server.releaseServer();
    }
}
