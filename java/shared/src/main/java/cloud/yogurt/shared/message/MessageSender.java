package cloud.yogurt.shared.message;

public interface MessageSender {
    void sendMessage(SendingMessage message) throws MessageException;
}
