package cloud.yogurt.shared.common;

public class YogurtException extends Exception {
    public YogurtException() {
        super("[YogurtException]");
    }

    public YogurtException(String s) {
        super("[YogurtException] " + s);
    }
}
