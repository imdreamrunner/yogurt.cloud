package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.client.servicecall.*;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.net.InetAddress;

public class YogurtServer {
    private static Logger log = Logger.getLogger(YogurtServer.class.getName());

    public YogurtServer(InetAddress address, int port) {
        server = new RemoteServer(address, port);
    }

    private static RemoteServer server;

    private FileCache fileCache = new FileCache();

    private boolean serverBusy = false;

    public void releaseServer() {
        serverBusy = false;
    }

    public boolean isServerBusy() {
        return serverBusy;
    }

    public void get(String path) {
        log.info("Trying to get " + path + " from server.");
        serverBusy = true;
        byte[] cached = fileCache.getCache(path, 10);
        if (cached == null) {
            if (fileCache.hasCache(path)) {
                // Has invalid cache.
                int callId = makeServiceCall(new CheckFileStatus(path));
                CacheUpdateHandler fileStatusHandler = new CacheUpdateHandler(path, fileCache, this);
                server.getMessageHandler().registerHandler(callId, fileStatusHandler);
            } else {
                // Not cached at all.
                int callId = makeServiceCall(new GetFileByPath(path));
                FileContentHandler fileContentHandler = new FileContentHandler(path, fileCache, this);
                server.getMessageHandler().registerHandler(callId, fileContentHandler);
            }
        } else {
            Logger.printRaw(new String(cached, SharedConfig.CONTENT_CHARSET));
            serverBusy = false;
        }
    }

    public void insert(String path, int offset, String fragment) {
        log.info("Trying to insert " + fragment + " to " + path + ".");
        makeServiceCall(new InsertFragment(path, offset, fragment));
    }

    public void monitor(String path) {
        log.info("Monitor " + path + ".");
        makeServiceCall(new MonitorFileChange(path, 1000));
    }

    public void delete(String path, int offset, int length) {
        log.info("Trying to delete " + length + " from " + path + " at " + offset + ".");
        makeServiceCall(new DeleteRange(path, offset, length));
    }

    public void check(String path) {
        log.info("Trying to check " + path + ".");
        makeServiceCall(new CheckFileStatus(path));
    }

    private static int makeServiceCall(ServiceCall call)  {
        return server.makeServiceCall(call);
    }

    public void stop() {
        server.stop();
    }
}
