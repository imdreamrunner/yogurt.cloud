package cloud.yogurt.shared.header;

public class HeaderStringValue extends HeaderValue {
    private String value;

    HeaderStringValue(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return this.value;
    }
}
