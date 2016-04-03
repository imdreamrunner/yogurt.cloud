package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.sharedconfig.SharedConfig;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.IOException;

public class HeaderDataLoader extends MessageDataLoader {
    private ByteInputStream byteInputStream;

    public HeaderDataLoader(Header header) {
        String headerAsString = header.toString();
        byte[] headerAsByte = headerAsString.getBytes(SharedConfig.HEADER_CHARSET);
        byteInputStream = new ByteInputStream(headerAsByte, headerAsByte.length);
    }

    @Override
    public int read() throws IOException {
        return byteInputStream.read();
    }
}
