package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.message.NonPayloadSendingMessage;
import cloud.yogurt.shared.network.EndPoint;

public class ServiceCall extends NonPayloadSendingMessage {
    private static int nextCallId = 1;
    private int callId;
    private EndPoint serverEndPoint;

    public ServiceCall(Header header) {
        super(header);
        callId = nextCallId;
        nextCallId ++;
    }

    @Override
    public int getCallId() {
        return callId;
    }

    public void setTarget(EndPoint endPoint) {
        this.serverEndPoint = endPoint;
    }

    @Override
    public EndPoint getTarget() {
        return serverEndPoint;
    }
}
