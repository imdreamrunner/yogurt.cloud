package cloud.yogurt.shared.network;


public class EndPointCall {
    public EndPoint endPoint;
    public int callId;

    public EndPointCall(EndPoint endPoint, int call) {
        this.endPoint = endPoint;
        this.callId = call;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof EndPointCall) {
            EndPointCall otherCall = (EndPointCall) other;
            if (callId == otherCall.callId &&
                    endPoint.address.equals(otherCall.endPoint.address) &&
                    endPoint.port == otherCall.endPoint.port) {
                return true;
            }
        }
        return false;
    }
}
