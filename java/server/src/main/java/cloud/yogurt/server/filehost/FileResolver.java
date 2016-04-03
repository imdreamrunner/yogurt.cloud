package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.message.MessageDataLoader;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileResolver extends MessageDataLoader {

    private File file;
    private FileInputStream fileInputStream;

    public FileResolver(String path) {
        file = new File(SharedConfig.SERVER_BASE_PATH + "/" + path);
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read() throws IOException {
        return fileInputStream.read();
    }
}
