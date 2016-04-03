package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.*;
import cloud.yogurt.shared.network.EndPoint;

public class ServiceCall extends PayloadSendingMessage {
    private static Logger log = Logger.getLogger(ServiceCall.class.getName());

    private static int nextCallId = 1;
    private int callId;
    private EndPoint serverEndPoint;
    private MessageDataLoader dataLoader;

    public ServiceCall(Header header) {
        super(header);
        callId = nextCallId;
        nextCallId ++;
        dataLoader = new EmptyDataLoader();

        log.debug("Service call header:\n" + header.toString());
    }

    public ServiceCall(Header header, byte[] data) {
        super(header);
        callId = nextCallId;
        nextCallId ++;
        dataLoader = new ByteDataLoader(data);

        log.debug("Service call header:\n" + header.toString());
    }

    @Override
    public MessageDataLoader getPayloadDataLoader() {
        return dataLoader;
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
