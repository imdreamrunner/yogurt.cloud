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
                FileContentHandler fileContentHandler = new FileContentHandler(path, fileCache, this, false);
                server.getMessageHandler().registerHandler(callId, fileContentHandler);
            }
        } else {
            Logger.printRaw(new String(cached, SharedConfig.CONTENT_CHARSET));
            serverBusy = false;
        }
    }


    public void get(String path, int offset, int limit) {
        byte[] cached = fileCache.getCache(path, 10);
        if (cached != null) {
            log.info("Trying to get " + path + " from cache.");
            try {
                if (offset + limit > cached.length) {
                    limit = Math.max(0, cached.length - offset);
                }
                byte[] partial = new byte[limit];
                System.arraycopy(cached, offset, partial, 0, limit);
                Logger.printRaw(new String(partial, SharedConfig.CONTENT_CHARSET));
            } catch (java.lang.ArrayIndexOutOfBoundsException ignored) {
                Logger.printRaw("Illegal parameter.");
            }
        } else {
            log.info("Trying to get " + path + " from server.");
            serverBusy = true;
            int callId = makeServiceCall(new GetFileByPath(path, offset, limit));
            FileContentHandler fileContentHandler = new FileContentHandler(path, fileCache, this, true);
            server.getMessageHandler().registerHandler(callId, fileContentHandler);
        }
    }

    public void insert(String path, int offset, String fragment) {
        log.info("Trying to insert " + fragment + " to " + path + ".");
        serverBusy = true;
        int callId = makeServiceCall(new InsertFragment(path, offset, fragment));
        server.getMessageHandler().registerHandler(callId, new OperationCallHandler(this));
    }

    public void monitor(String path) {
        log.info("Monitor " + path + ".");
        serverBusy = true;
        int callId = makeServiceCall(new MonitorFileChange(path, 1000));
        server.getMessageHandler().registerHandler(callId, new MonitorHandler(path));
    }

    public void delete(String path, int offset, int length) {
        log.info("Trying to delete " + length + " from " + path + " at " + offset + ".");
        serverBusy = true;
        int callId = makeServiceCall(new DeleteRange(path, offset, length));
        server.getMessageHandler().registerHandler(callId, new OperationCallHandler(this));
    }

    public void check(String path) {
        log.info("Trying to check " + path + ".");
        serverBusy = true;
        int callId = makeServiceCall(new CheckFileStatus(path));
        server.getMessageHandler().registerHandler(callId, new CheckStatusHandler(path, this));
    }

    private static int makeServiceCall(ServiceCall call)  {
        return server.makeServiceCall(call);
    }

    public void stop() {
        server.stop();
    }
}
