package cloud.yogurt.shared.common;

public class YogurtException extends Exception {
    private int error;

    public YogurtException() {
        super("[YogurtException]");
    }

    public YogurtException(String s) {
        super("[YogurtException] " + s);
    }

    public YogurtException(int error, String s) {
        super("[YogurtException] " + s);
    }

    public int getError(){
        return error;
    }
}
