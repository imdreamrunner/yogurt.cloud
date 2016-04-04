package cloud.yogurt.shared.header;

/**
 * Integer value in headers
 */
public class HeaderIntegerValue extends HeaderValue {
    private long value;

    public HeaderIntegerValue(long value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "" + this.value;
    }
    public long getValue() {
        return value;
    }
}
