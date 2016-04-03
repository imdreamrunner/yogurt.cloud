package cloud.yogurt.shared.message;

import java.io.SequenceInputStream;
import cloud.yogurt.shared.header.Header;

public abstract class PayloadSendingMessage extends SendingMessage {
    private Header header;

    public PayloadSendingMessage(Header header) {
        this.header = header;
    }

    public abstract MessageDataLoader getPayloadDataLoader();

    protected final MessageDataLoader getMessageDataLoader() {
        HeaderDataLoader headerDataLoader = new HeaderDataLoader(header);
        return new CombineDataLoader(headerDataLoader, getPayloadDataLoader());
    }
}
