package cloud.yogurt.shared.header;

/**
 * String value in header
 */
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
