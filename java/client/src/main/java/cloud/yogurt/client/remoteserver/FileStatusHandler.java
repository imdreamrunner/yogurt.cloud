package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;

/**
 * Created by shengliangl on 4/10/2016.
 */
public class FileStatusHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    FileCache cache;

    public FileStatusHandler(String filename, FileCache cache) {
        this.filename = filename;
        this.cache = cache;
    }

    /**
     * handle check file status
     * @param receivingMessage
     */
    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        log.info("Handle file status, and check cache validation.");
        if (receivingMessage.header.getValue("LastModify") != null) {
            if (cache.getModifyTime(filename)
                    != ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue()) {
                log.info("Cache timeout. Please re-get file");
                cache.timeoutCache(filename);
            }
        }
    }
}
