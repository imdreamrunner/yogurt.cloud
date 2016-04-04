package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;

import java.util.ArrayList;

public class CheckFileStatus extends ServiceCall {
    public CheckFileStatus(String path) {
        super(new Header(new String[]{"CHECK", path}, new ArrayList<>()));
    }
}
