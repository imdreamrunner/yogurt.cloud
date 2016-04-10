package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

/**
 * Handle the content of file received from the server.
 */
public class FileContentHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    FileCache cache;
    YogurtServer server;

    public FileContentHandler(String filename, FileCache cache, YogurtServer server) {
        this.filename = filename;
        this.cache = cache;
        this.server = server;
    }

    /**
     * Handle the receiving message from the server.
     * @param receivingMessage the message just received.
     */
    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        if (receivingMessage.header.getValue("LastModify") != null) {
            log.info("Handle file content, and update cache.");
            this.cache.updateCache(filename,
                    receivingMessage.payload,
                    ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue());
            Logger.printRaw(new String(receivingMessage.payload, SharedConfig.CONTENT_CHARSET));
            this.server.releaseServer();
        } else {
            Logger.printRaw("File does not exist.");
            this.server.releaseServer();
        }
    }
}
