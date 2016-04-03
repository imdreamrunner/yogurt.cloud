package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.network.EndPoint;

public class ReceivingMessage {
    public int callId;
    public EndPoint source;
    public Header header;
    public byte[] payload;

    public ReceivingMessage(int callId, EndPoint endPoint, Header header, byte[] payload) {
        this.callId = callId;
        this.source = endPoint;
        this.header = header;
        this.payload = payload;
    }
}
