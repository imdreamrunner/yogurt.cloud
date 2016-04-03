package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

public class FileHost {
    private static Logger log = Logger.getLogger(FileHost.class.getName());

    public FileResolver get(String filename) throws FileNotFoundException {
        FileResolver fileResolver = new FileResolver(filename);
        return fileResolver;
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

    public void insert(String filename, long offset, byte[] fragment) throws IOException {
        log.debug("Insert " + new String(fragment, SharedConfig.CONTENT_CHARSET) + " into " + filename +
                " offset " + offset);

        for (byte b: fragment
                ) {
            System.out.println("BBBBBBBB>>>>>>>>> " + b);
        }

        File file = new File(SharedConfig.SERVER_BASE_PATH + "/" + filename);
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.WRITE);
        channel.position(offset);
        channel.write(ByteBuffer.wrap(fragment));
    }
}
