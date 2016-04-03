package cloud.yogurt.client.cmd;

import cloud.yogurt.client.remoteserver.RemoteServer;
import cloud.yogurt.client.servicecall.GetFileByPath;
import cloud.yogurt.client.servicecall.ServiceCall;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.IOException;
import java.util.Scanner;

public class YogurtClient {
    private static Logger log = Logger.getLogger(YogurtClient.class.getName());

    private static RemoteServer server;

    public static void main(String[] args) throws IOException {
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
                    System.exit(0);
                }
                case "get": {
                    String path = components[1];
                    log.info("Trying to get " + path + " from server.");
                    makeServiceCall(new GetFileByPath(path));
                }
            }
        }
    }

    private static void makeServiceCall(ServiceCall call) {

    }
}
