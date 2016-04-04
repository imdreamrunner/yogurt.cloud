package cloud.yogurt.shared.header;

/**
 * Passed parameters are listed in rows
 * With format of key and value pairs
 */
public class HeaderRow {
    public String key;
    public HeaderValue value;

    public HeaderRow(String key, HeaderValue value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return key + ": " + value.toString();
    }
}
