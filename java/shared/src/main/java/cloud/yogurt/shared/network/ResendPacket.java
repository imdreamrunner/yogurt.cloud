package cloud.yogurt.shared.network;

import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;
import cloud.yogurt.shared.time.SetTimeout;
import cloud.yogurt.shared.time.TimeoutHandler;

public class ResendPacket implements TimeoutHandler {
    private static Logger log = Logger.getLogger(ResendPacket.class.getName());

    private Packet packet;
    private DatagramServer datagramServer;
    private int retry;

    ResendPacket(Packet packet, DatagramServer datagramServer, int retry) {
        this.packet = packet;
        this.datagramServer = datagramServer;
        this.retry = retry;
    }

    @Override
    public void handleTimeout() {
        if (!this.datagramServer.isPacketReceived(this.packet)) {
            try {
                if (this.datagramServer.isNextToSend(packet)) {
                    retry ++;
                    if (retry > SharedConfig.MAXIMUM_RETRY) {
                        log.error("TIMEOUT", "Unable to send packet " + packet + " after " + retry + " times.");
                        return;
                    }
                    log.info("TIMEOUT_RESEND", "Resent packet " + packet + " after timeout. Retry = " + retry);
                    this.datagramServer.sendPacket(this.packet, retry);
                } else {
                    SetTimeout.setTimeout(this, 10);
                }
            } catch (PacketException e) {
                e.printStackTrace();
            }
        }
    }
}
