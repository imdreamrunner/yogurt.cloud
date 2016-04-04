package cloud.yogurt.client.servicecall;


import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;

import java.util.ArrayList;

/**
 * Service call to get content of a file.
 */
public class GetFileByPath extends ServiceCall {
    public GetFileByPath(String path) {
        super(new Header(new String[]{"GET", path}, new ArrayList<>()));
    }

    public GetFileByPath(String path, int offset, int limit) {
        super(new Header(new String[]{"GET", path}, new ArrayList<HeaderRow>() {
            {
                add(new HeaderRow("Offset", new HeaderIntegerValue(offset)));
                add(new HeaderRow("Limit", new HeaderIntegerValue(limit)));
            }
        }));

    }
}
