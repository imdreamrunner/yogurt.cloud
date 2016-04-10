package cloud.yogurt.client.cmd;

import cloud.yogurt.client.filecache.FileCache;
import cloud.yogurt.client.remoteserver.RemoteServer;
import cloud.yogurt.client.remoteserver.YogurtServer;
import cloud.yogurt.client.servicecall.*;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * Entry point to YogurtCloud client application.
 */
public class YogurtClient {
    private static Logger log = Logger.getLogger(YogurtClient.class.getName());

    private static YogurtServer server;

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        log.info("Starting Yogurt Client");

        Logger.silent = false;

        InetAddress address = SharedConfig.SERVER_ADDRESS;
        int port = SharedConfig.SERVER_PORT;

        if (args.length > 0) {
            String serverHost = args[0];
            address = InetAddress.getByName(serverHost);
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
        }

        server = new YogurtServer(address, port);

        Logger.printRaw("Welcome to Yogurt Cloud. Server: " + address.toString() + ":" + port);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            while (server.isServerBusy()) {
                Thread.sleep(100);
            }

            System.out.print(">> ");
            String command = scanner.nextLine();
            if (command.length() == 0) continue;

            String[] components = command.split(" ");
            String function = components[0];

            try {
                switch (function) {
                    case "exit": {
                        System.out.println("Bye.");
                        server.stop();
                        System.exit(0);
                    }
                    case "silent": {
                        Logger.silent = true;
                        break;
                    }
                    case "get": {
                        String path = components[1];
                        if (components.length > 2) {
                            int offset = Integer.parseInt(components[2]);
                            int limit = Integer.parseInt(components[3]);
                            server.get(path, offset, limit);
                        } else {
                            server.get(path);
                        }
                        break;
                    }
                    case "insert": {
                        String path = components[1];
                        int offset = Integer.parseInt(components[2]);
                        String fragment = components[3];
                        server.insert(path, offset, fragment);
                        break;
                    }
                    case "monitor": {
                        String path = components[1];
                        server.monitor(path);
                        break;
                    }
                    case "delete": {
                        String path = components[1];
                        int offset = Integer.parseInt(components[2]);
                        int length = Integer.parseInt(components[3]);
                        server.delete(path, offset, length);
                        break;
                    }
                    case "check": {
                        String path = components[1];
                        server.check(path);
                        break;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException | NumberFormatException ignored) {
                Logger.printRaw("Illegal parameters. Please check.");
            }
        }
    }
}
