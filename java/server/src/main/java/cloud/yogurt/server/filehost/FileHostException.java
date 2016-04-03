package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.common.YogurtException;

public class FileHostException extends YogurtException {
    public FileHostException(int error, String message) {
        super(error, "FileHostException: " + message);
    }
}
