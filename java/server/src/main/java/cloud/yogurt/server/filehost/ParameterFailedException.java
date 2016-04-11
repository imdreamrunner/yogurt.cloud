package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.common.YogurtException;

public class ParameterFailedException extends YogurtException {
    public ParameterFailedException(int error, String message) {
        super(error, "ParameterFailedException: " + message);
    }
}
