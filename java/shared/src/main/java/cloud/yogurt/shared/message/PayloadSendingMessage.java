package cloud.yogurt.shared.message;

import java.io.IOException;
import java.io.SequenceInputStream;
import cloud.yogurt.shared.header.Header;

/**
 * Payload message sending with combined data loader
 */
public abstract class PayloadSendingMessage extends SendingMessage {
    private Header header;

    public PayloadSendingMessage(Header header) {
        this.header = header;
    }

    public abstract MessageDataLoader getPayloadDataLoader();

    public MessageDataLoader getMessageDataLoader() {
        HeaderDataLoader headerDataLoader = new HeaderDataLoader(header);
        return new CombineDataLoader(headerDataLoader, getPayloadDataLoader());
    }
}
