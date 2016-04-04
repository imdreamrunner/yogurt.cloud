package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;

import java.util.ArrayList;

public class DeleteRange extends ServiceCall {

    public DeleteRange(String path, int offset, int limit) {
        super(new Header(new String[]{"DELETE", path}, new ArrayList<HeaderRow>() {
            {
                add(new HeaderRow("Offset", new HeaderIntegerValue(offset)));
                add(new HeaderRow("Length", new HeaderIntegerValue(limit)));
            }
        }));
    }

}
