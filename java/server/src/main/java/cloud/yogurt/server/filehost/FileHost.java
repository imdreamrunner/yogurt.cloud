package cloud.yogurt.server.filehost;

import java.io.IOException;

public class FileHost {

    public FileResolver get(String filename, long offset, long limit) throws FileHostException {
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

}
