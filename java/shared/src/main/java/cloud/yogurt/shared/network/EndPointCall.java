package cloud.yogurt.shared.network;


public class EndPointCall {
    public EndPoint endPoint;
    public int callId;

    public EndPointCall(EndPoint endPoint, int call) {
        this.endPoint = endPoint;
        this.callId = call;
    }

    public EndPointCall(Packet packet) {
        this.endPoint = packet.endPoint;
        this.callId = packet.callId;
    }

    /**
     * Message control packet equal checker
     * @param other
     * @return
     */
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

    public int hashCode() {
        return callId;
    }

    public String toString() {
        return this.endPoint.toString() + "->" + callId;
    }
}
