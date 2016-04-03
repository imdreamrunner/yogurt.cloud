package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;

public abstract class NonPayloadSendingMessage extends PayloadSendingMessage {
    public NonPayloadSendingMessage(Header header) {
        super(header);
    }

    @Override
    public MessageDataLoader getPayloadDataLoader() {
        return new EmptyDataLoader();
    }
}
