package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.message.NonPayloadSendingMessage;
import cloud.yogurt.shared.network.PacketSender;
import cloud.yogurt.shared.network.EndPoint;

public class ServiceCall extends NonPayloadSendingMessage {
    private int nextCallId = 0;
    private EndPoint serverEndPoint;

    public ServiceCall(Header header) {
        super(header);
    }

    @Override
    public int getCallId() {
        nextCallId ++;
        return nextCallId - 1;
    }

    public void setTarget(EndPoint endPoint) {
        this.serverEndPoint = endPoint;
    }

    @Override
    public EndPoint getTarget() {
        return serverEndPoint;
    }
}
