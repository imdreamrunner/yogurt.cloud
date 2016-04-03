package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.message.MessageDataLoader;
import cloud.yogurt.shared.message.MessageTarget;
import cloud.yogurt.shared.message.SendingMessage;
import cloud.yogurt.shared.message.PacketSender;

public class RequestMessage extends SendingMessage {
    @Override
    public MessageTarget getMessageTarget() {
        return null;
    }

    @Override
    protected PacketSender getMessagePacketHandler() {
        return null;
    }

    @Override
    protected MessageDataLoader getMessageDataLoader() {
        return null;
    }
}
