package cloud.yogurt.shared.message;

import cloud.yogurt.shared.common.YogurtException;

public class MessageException extends YogurtException {
    public MessageException(String s) {
        super("MessageException: " + s);
    }
}
