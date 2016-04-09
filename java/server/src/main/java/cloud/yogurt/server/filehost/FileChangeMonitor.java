package cloud.yogurt.server.filehost;

import cloud.yogurt.server.serverhost.ServerHost;
import cloud.yogurt.server.serverhost.ServerResponse;
import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.PacketException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A class holding information to be executed after file change event.
 */
public class FileChangeMonitor {
    private static Logger log = Logger.getLogger(FileChangeMonitor.class.getName());

    private EndPoint endPoint;
    private int callId;
    private int duration;
    private String filename;

    FileChangeMonitor(String filename, int duration, EndPoint endPoint, int callId) {
        log.debug("File change monitor is created.");

        this.filename = filename;
        this.duration = duration;
        this.endPoint = endPoint;
        this.callId = callId;
    }

    public void fileChange() {
        FileResolver fileResolver = null;
        try {
            fileResolver = new FileResolver(filename);

            ServerHost.getInstance().getHostThread().sendMessage(new ServerResponse(
                    callId,
                    new Header(
                            new String[] {"NOTICE", filename},
                            new ArrayList<>()
                    ),
                    fileResolver,
                    endPoint
            ));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
