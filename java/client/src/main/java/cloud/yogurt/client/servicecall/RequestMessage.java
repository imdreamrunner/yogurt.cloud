package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.message.MessageLoadDataHandler;
import cloud.yogurt.shared.message.MessageTarget;
import cloud.yogurt.shared.message.SendingMessage;
import cloud.yogurt.shared.message.SendingPacketHandler;

public class RequestMessage extends SendingMessage {
    @Override
    public MessageTarget getMessageTarget() {
        return null;
    }

    @Override
    protected SendingPacketHandler getMessagePacketHandler() {
        return null;
    }

    @Override
    protected MessageLoadDataHandler getMessageLoadDataHandler() {
        return null;
    }
}
