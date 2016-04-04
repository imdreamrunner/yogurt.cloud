package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Message data loader for packet header
 */
public class HeaderDataLoader extends MessageDataLoader {
    private ByteArrayInputStream byteInputStream;

    public HeaderDataLoader(Header header) {
        String headerAsString = header.toString();
        byte[] headerAsByte = headerAsString.getBytes(SharedConfig.HEADER_CHARSET);
        byteInputStream = new ByteArrayInputStream(headerAsByte);
    }

    @Override
    public int read() throws IOException {
        return byteInputStream.read();
    }
}
