package cloud.yogurt.shared.message;

import java.io.IOException;

public class EmptyDataLoader extends MessageDataLoader {
    @Override
    public int read() throws IOException {
        return -1;
    }
}
