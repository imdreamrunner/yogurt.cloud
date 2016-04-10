package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;

/**
 * File status handler for file status update.
 * Created by shengliangl on 4/10/2016.
 */
public class CacheUpdateHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    FileCache cache;
    YogurtServer server;

    public CacheUpdateHandler(String filename, FileCache cache, YogurtServer server) {
        this.filename = filename;
        this.cache = cache;
        this.server = server;
    }

    /**
     * handle check file status
     * @param receivingMessage message received.
     */
    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.info("Handle file status, and check cache validation.");
        if (receivingMessage.header.getValue("LastModify") != null) {
            log.info("Last update time for file " + filename + " is " +
                    ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue());
            if (cache.getModifyTime(filename)
                    != ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue()) {
                log.info("Timeout cache for " + filename + ".");
                cache.timeoutCache(filename);
            } else {
                log.info("Refresh cache for " + filename + ".");
                cache.refreshCache(filename);
            }
            server.get(filename);
        } else {
            log.error("Unknown exception.");
            server.releaseServer();
        }
    }
}
