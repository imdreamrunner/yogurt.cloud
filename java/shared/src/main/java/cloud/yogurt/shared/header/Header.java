package cloud.yogurt.shared.header;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.util.ArrayList;
import java.util.List;

public class Header {
    private static Logger log = Logger.getLogger(Header.class.getName());

    private String[] params;
    private  List<HeaderRow> headerRows;

    public Header(String[] params, List<HeaderRow> headerRows) {
        this.params = params;
        this.headerRows = headerRows;
    }

    public String[] getParams() {
        return this.params;
    }

    public HeaderValue getValue(String key) {
        for (HeaderRow row : headerRows) {
            if (row.key.equals(key)) {
                return row.value;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuffer headerString = new StringBuffer(SharedConfig.CURRENT_API_VERSION);
        for (String param: params) {
            headerString.append(' ');
            headerString.append(param);
        }
        headerString.append('\n');
        for (HeaderRow row : headerRows) {
            headerString.append(row.toString());
            headerString.append('\n');
        }
        headerString.append('\n');

        return headerString.toString();
    }


    public static Header fromString(String string) {
        String[] lines = string.split("\n");
        String[] firstLines = lines[0].split(" ");
        String[] params = new String[firstLines.length - 1];
        System.arraycopy(firstLines, 1, params, 0, firstLines.length - 1);
        List<HeaderRow> rows = new ArrayList<>();
        for (int i = 1; i < lines.length; i ++) {
            if (lines[i].length() > 0) {
                String[] keyValuePair = lines[i].split(": ");
                String key = keyValuePair[0];
                String value = keyValuePair[1];
                HeaderValue headerValue;
                try {
                    long intValue = Long.parseUnsignedLong(value);
                    headerValue = new HeaderIntegerValue(intValue);
                } catch (NumberFormatException ignored) {
                    headerValue = new HeaderStringValue(value);
                }
                rows.add(new HeaderRow(key, headerValue));
            }
        }
        Header header = new Header(params, rows);
        return header;
    }
}
