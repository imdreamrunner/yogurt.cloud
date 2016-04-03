package cloud.yogurt.shared.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteDataLoader extends MessageDataLoader {
    private ByteArrayInputStream byteArrayInputStream;

    public ByteDataLoader(byte[] data) {
        byteArrayInputStream = new ByteArrayInputStream(data);
    }

    @Override
    public int read() throws IOException {
        return byteArrayInputStream.read();
    }
}
