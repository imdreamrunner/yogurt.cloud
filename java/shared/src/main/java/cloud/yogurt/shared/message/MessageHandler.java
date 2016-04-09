package cloud.yogurt.shared.message;

public interface MessageHandler {
    void handleMessage(ReceivingMessage receivingMessage);
}
