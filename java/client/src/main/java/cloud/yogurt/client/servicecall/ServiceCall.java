package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.message.NonPayloadSendingMessage;
import cloud.yogurt.shared.message.PacketSender;
import cloud.yogurt.shared.network.EndPoint;

public class ServiceCall extends NonPayloadSendingMessage {
    private int nextCallId = 0;

    public ServiceCall(Header header) {
        super(header);
    }

    @Override
    public int getCallId() {
        nextCallId ++;
        return nextCallId - 1;
    }


    @Override
    public EndPoint getMessageTarget() {
        return null;
    }

    @Override
    protected PacketSender getPacketSender() {
        return null;
    }
}
