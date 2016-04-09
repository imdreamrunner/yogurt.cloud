package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.network.PacketException;

import java.io.IOException;

/**
 * Handle the content of file received from the server.
 */
public class FileContentHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    FileCache cache;

    public FileContentHandler(String filename, FileCache cache) {
        this.filename = filename;
        this.cache = cache;
    }

    /**
     * Handle the receiving message from the server.
     * @param receivingMessage the message just received.
     */
    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.info("Handle file content, and update cache.");
        if (receivingMessage.header.getValue("LastModify") != null) {
            this.cache.updateCache(filename,
                    receivingMessage.payload,
                    ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue());
        }
    }
}
