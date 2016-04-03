package cloud.yogurt.shared.header;

public class HeaderIntegerValue extends HeaderValue {
    private int value;

    public HeaderIntegerValue(int value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return "" + this.value;
    }
}
