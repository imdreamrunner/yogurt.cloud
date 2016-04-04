package cloud.yogurt.server.serverhost;

import cloud.yogurt.server.filehost.FileHost;
import cloud.yogurt.server.filehost.FileResolver;
import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.EmptyDataLoader;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.network.PacketException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ClientMessageHandler implements MessageHandler {
    private ServerHostThread getServerHostThread () {
        return ServerHost.getInstance().getHostThread();
    }
    private static Logger log = Logger.getLogger(ClientMessageHandler.class.getName());
    private static FileHost fileHost = new FileHost();

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) throws IOException, PacketException {
        log.debug("Receive message from client.");

        String action = receivingMessage.header.getParams()[0];

        switch (action) {
            case "GET": {
                String path = receivingMessage.header.getParams()[1];
                try {
                    FileResolver fileResolver = fileHost.get(path);
                    Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<>());
                    ServerResponse response = new ServerResponse(receivingMessage.callId, header,
                            fileResolver, receivingMessage.source);
                    this.getServerHostThread().sendMessage(response);
                } catch (FileNotFoundException unused) {
                    Header header = new Header(new String[]{"STATUS", "ERROR"}, new ArrayList<>());
                    ServerResponse response = new ServerResponse(receivingMessage.callId, header,
                            new EmptyDataLoader(), receivingMessage.source);
                    this.getServerHostThread().sendMessage(response);
                }
                break;
            }
            case "INSERT": {
                String path = receivingMessage.header.getParams()[1];
                int offset = (int)((HeaderIntegerValue)receivingMessage.header.getValue("Offset")).getValue();
                byte[] data = receivingMessage.payload;
                fileHost.insert(path, offset, data);
                Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<>());
                ServerResponse response = new ServerResponse(receivingMessage.callId, header,
                        new EmptyDataLoader(), receivingMessage.source);
                this.getServerHostThread().sendMessage(response);
                break;
            }
            case "MONITOR": {
                String path = receivingMessage.header.getParams()[1];
                int duration = (int)((HeaderIntegerValue)receivingMessage.header.getValue("Duration")).getValue();
                fileHost.monitor(path, duration, receivingMessage.source, receivingMessage.callId);
                break;
            }
            case "DELETE": {
                String path = receivingMessage.header.getParams()[1];
                int offset = (int)((HeaderIntegerValue)receivingMessage.header.getValue("Offset")).getValue();
                int length = (int)((HeaderIntegerValue)receivingMessage.header.getValue("Length")).getValue();
                fileHost.delete(path, offset, length);
                Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<>());
                ServerResponse response = new ServerResponse(receivingMessage.callId, header,
                        new EmptyDataLoader(), receivingMessage.source);
                this.getServerHostThread().sendMessage(response);
                break;
            }
            case "CHECK": {
                String path = receivingMessage.header.getParams()[1];
                boolean exist;
                try {
                    FileResolver fileResolver = fileHost.get(path);
                    exist = true;
                } catch (FileNotFoundException unused) {
                    exist = false;
                }
                if (exist) {
                    Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<HeaderRow>() {
                        {
                            add(new HeaderRow("LastModify",
                                    new HeaderIntegerValue( fileHost.getLastModify(path))));
                        }
                    });
                    ServerResponse response = new ServerResponse(receivingMessage.callId, header,
                            new EmptyDataLoader(), receivingMessage.source);
                    this.getServerHostThread().sendMessage(response);
                }
                break;
            }
        }
    }
}
