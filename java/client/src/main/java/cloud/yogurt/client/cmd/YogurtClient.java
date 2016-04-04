package cloud.yogurt.client.cmd;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.client.remoteserver.FileContentHandler;
import cloud.yogurt.client.remoteserver.RemoteServer;
import cloud.yogurt.client.servicecall.*;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.Scanner;


/**
 * Entry point to YogurtCloud client application.
 */
public class YogurtClient {
    private static Logger log = Logger.getLogger(YogurtClient.class.getName());

    private static RemoteServer server;

    private static FileCache fileCache = new FileCache();

    public static void main(String[] args) throws IOException, PacketException {
        log.info("Starting Yogurt Client");

        server = new RemoteServer(SharedConfig.SERVER_ADDRESS, SharedConfig.SERVER_PORT);

        System.out.println("Welcome to Yogurt Cloud.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">> ");
            String command = scanner.nextLine();
            if (command.length() == 0) continue;

            String[] components = command.split(" ");
            String function = components[0];

            switch (function) {
                case "exit": {
                    System.out.println("Bye.");
                    server.stop();
                    System.exit(0);
                }
                case "get": {
                    String path = components[1];
                    log.info("Trying to get " + path + " from server.");
                    if (fileCache.getCache(path) == null) {
                        int callId = makeServiceCall(new GetFileByPath(path));
                        FileContentHandler fileContentHandler = new FileContentHandler(path, fileCache);
                        server.getMessageHandler().registerHandler(callId, fileContentHandler);
                    } else {
                        System.out.println("File in cache:");
                        System.out.println(new String(fileCache.getCache(path), SharedConfig.CONTENT_CHARSET));
                    }
                    break;
                }
                case "insert": {
                    String path = components[1];
                    int offset = Integer.parseInt(components[2]);
                    String fragment = components[3];
                    log.info("Trying to insert " + fragment + " to " + path + ".");
                    makeServiceCall(new InsertFragment(path, offset, fragment));
                    break;
                }
                case "monitor": {
                    String path = components[1];
//                    int duration = Integer.parseInt(components[2]);
                    log.info("Monitor " + path + ".");
                    makeServiceCall(new MonitorFileChange(path, 10));
                    break;
                }
                case "delete": {
                    String path = components[1];
                    int offset = Integer.parseInt(components[2]);
                    int length = Integer.parseInt(components[3]);
                    log.info("Trying to delete " + length + " from " + path + " at " + offset + ".");
                    makeServiceCall(new DeleteRange(path, offset, length));
                    break;
                }
                case "check": {
                    String path = components[1];
                    log.info("Trying to check " + path + ".");
                    makeServiceCall(new CheckFileStatus(path));
                    break;
                }
            }
        }
    }

    private static int makeServiceCall(ServiceCall call) throws IOException, PacketException {
        return server.makeServiceCall(call);
    }
}
