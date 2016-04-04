package cloud.yogurt.server.serverhost;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.message.MessageDataLoader;
import cloud.yogurt.shared.message.PayloadSendingMessage;
import cloud.yogurt.shared.network.EndPoint;

/**
 * Sending message constructor for response to the client.
 */
public class ServerResponse extends PayloadSendingMessage {

    private int callId;
    private MessageDataLoader dataLoader;
    private EndPoint endPoint;


    public ServerResponse(int callId, Header header, MessageDataLoader dataLoader, EndPoint target) {
        super(header);
        this.callId = callId;
        this.dataLoader = dataLoader;
        this.endPoint = target;
    }

    @Override
    public MessageDataLoader getPayloadDataLoader() {
        return dataLoader;
    }

    @Override
    public int getCallId() {
        return callId;
    }

    @Override
    public EndPoint getTarget() {
        return this.endPoint;
    }
}
