package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileHost {
    private static Logger log = Logger.getLogger(FileHost.class.getName());

    private Map<String, List<FileChangeMonitor>> monitors = new HashMap<>();

    public FileResolver get(String filename) throws FileNotFoundException {
        return new FileResolver(filename);
    }

    public FileResolver get(String filename, long offset, long limit) throws FileHostException, FileNotFoundException {
        FileResolver fileResolver = new FileResolver(filename);
        long actualSkipped;
        try {
            actualSkipped = fileResolver.skip(offset);
        } catch (IOException e) {
            throw new FileHostException(100, "Cannot read file.");
        }
        if (actualSkipped != offset) {
            throw new FileHostException(101, "Cannot skip file.");
        }
        return fileResolver;
    }

    public void insert(String filename, int offset, byte[] fragment) throws IOException, PacketException {
        log.debug("Insert " + new String(fragment, SharedConfig.CONTENT_CHARSET) + " into " + filename +
                " offset " + offset);

        File file = new File(SharedConfig.SERVER_BASE_PATH + "/" + filename);
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] modified = new byte[data.length + fragment.length];

        System.arraycopy(data, 0, modified, 0, offset);
        System.arraycopy(fragment, 0, modified, offset, fragment.length);
        System.arraycopy(data, offset, modified, offset + fragment.length, data.length - offset);

        Files.write(file.toPath(), modified);

        notifyMonitor(filename);
    }

    private void notifyMonitor(String filename) throws IOException, PacketException {
        if (monitors.get(filename) != null) {
            for (FileChangeMonitor monitor: monitors.get(filename) ) {
                monitor.fileChange();
            }
        }
    }

    public void monitor(String filename, int duration, EndPoint endPoint, int callId) {
        log.info("Start monitor " + filename + " from " + endPoint + ".");

        if (monitors.get(filename) == null) {
            monitors.put(filename, new ArrayList<>());
        }
        monitors.get(filename).add(new FileChangeMonitor(filename, duration, endPoint, callId));
    }

    public void delete(String filename, int offset, int length) throws IOException, PacketException {
        log.info("Delete " + filename + " from " + offset + " for " + length + ".");

        File file = new File(SharedConfig.SERVER_BASE_PATH + "/" + filename);
        byte[] data = Files.readAllBytes(file.toPath());
        byte[] modified = new byte[data.length - length];

        System.arraycopy(data, 0, modified, 0, offset);
        System.arraycopy(data, offset + length, modified, offset, data.length - offset - length);

        Files.write(file.toPath(), modified);

        notifyMonitor(filename);
    }

    public long getLastModify(String filename) {
        File file = new File(SharedConfig.SERVER_BASE_PATH + "/" + filename);
        return file.lastModified();
    }
}
