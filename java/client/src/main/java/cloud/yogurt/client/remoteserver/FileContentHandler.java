package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.network.PacketException;

import java.io.IOException;

public class FileContentHandler implements MessageHandler {
    private Logger log = Logger.getLogger(FileContentHandler.class.getName());

    String filename;
    FileCache cache;

    public FileContentHandler(String filename, FileCache cache) {
        this.filename = filename;
        this.cache = cache;
    }

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) throws IOException, PacketException {
        log.info("Handle file content, and update cache.");
        this.cache.updateCache(filename,
                receivingMessage.payload,
                ((HeaderIntegerValue)receivingMessage.header.getValue("LastModify")).getValue());
    }
}
