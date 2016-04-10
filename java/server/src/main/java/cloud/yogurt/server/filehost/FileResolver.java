package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.message.MessageDataLoader;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A data loader for file in file host.
 */
public class FileResolver extends MessageDataLoader {

    private long hasRead = 0;
    private long limit = Long.MAX_VALUE;
    private File file;
    private FileInputStream fileInputStream;

    public FileResolver(String path) throws FileNotFoundException {
        file = new File(SharedConfig.SERVER_BASE_PATH + "/" + path);
        fileInputStream = new FileInputStream(file);
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        hasRead ++;
        if (hasRead > limit) return -1;
        return fileInputStream.read();
    }
}
