package cloud.yogurt.server.serverhost;

import cloud.yogurt.server.dulplicatefilter.DuplicateFilter;
import cloud.yogurt.server.filehost.FileHost;
import cloud.yogurt.server.filehost.FileHostException;
import cloud.yogurt.server.filehost.FileResolver;
import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.message.EmptyDataLoader;
import cloud.yogurt.shared.message.MessageDataLoader;
import cloud.yogurt.shared.message.MessageHandler;
import cloud.yogurt.shared.message.ReceivingMessage;
import cloud.yogurt.shared.network.EndPointCall;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The general handler for messages from client.
 */
public class ClientMessageHandler implements MessageHandler {
    private ServerHostThread getServerHostThread () {
        return ServerHost.getInstance().getHostThread();
    }
    private static Logger log = Logger.getLogger(ClientMessageHandler.class.getName());
    private static FileHost fileHost = new FileHost();
    private static DuplicateFilter filter = new DuplicateFilter();

    private void sendMessage(ServerResponse message) {
        this.getServerHostThread().sendMessage(message);
    }

    private void respondClient(ReceivingMessage message, Header header, MessageDataLoader dataLoader) {
        ServerResponse response = new ServerResponse(message.callId, header, dataLoader, message.source);
        this.sendMessage(response);
    }

    private void respondClientSuccess(ReceivingMessage message, MessageDataLoader dataLoader) {
        Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<>());
        respondClient(message, header, dataLoader);
    }

    private void respondClientSuccess(ReceivingMessage message) {
        respondClientSuccess(message, new EmptyDataLoader());
    }

    private void respondClientError(ReceivingMessage message, MessageDataLoader dataLoader) {
        Header header = new Header(new String[]{"STATUS", "ERROR"}, new ArrayList<>());
        respondClient(message, header, dataLoader);
    }

    private void respondClientError(ReceivingMessage message) {
        respondClientError(message, new EmptyDataLoader());
    }

    @Override
    public void handleMessage(ReceivingMessage receivingMessage) {
        EndPointCall endPointCall = new EndPointCall(receivingMessage.source, receivingMessage.callId);

        log.debug("Receive message from client " + endPointCall + ".");

        if (filter.isDuplicate(endPointCall)) {
            log.info("Receive duplicate call " + endPointCall
                    + ". Drop call.");
            return;
        }
        filter.mark(new EndPointCall(receivingMessage.source, receivingMessage.callId));

        String action = receivingMessage.header.getParams()[0];

        switch (action) {
            case "GET": {
                String path = receivingMessage.header.getParams()[1];
                boolean withLimit = false;
                long limit = 0;
                long offset = 0;
                if (receivingMessage.header.getValue("Limit") != null) {
                    limit = ((HeaderIntegerValue)receivingMessage.header.getValue("Limit")).getValue();
                    offset = ((HeaderIntegerValue)receivingMessage.header.getValue("Offset")).getValue();
                    withLimit = true;
                }
                try {
                    FileResolver fileResolver;
                    if (withLimit) {
                        fileResolver = fileHost.get(path, offset, limit);
                    } else {
                        fileResolver = fileHost.get(path);
                    }

                    Header header = new Header(new String[]{"STATUS", "SUCCESS"}, new ArrayList<HeaderRow>(){
                        {
                            {
                                add(new HeaderRow("LastModify",
                                        new HeaderIntegerValue( fileHost.getLastModify(path))));
                            }
                        }
                    });
                    this.respondClient(receivingMessage, header, fileResolver);
                } catch (FileNotFoundException | FileHostException unused) {
                    this.respondClientError(receivingMessage);
                }
                break;
            }
            case "INSERT": {
                String path = receivingMessage.header.getParams()[1];
                int offset = (int)((HeaderIntegerValue)receivingMessage.header.getValue("Offset")).getValue();
                byte[] data = receivingMessage.payload;
                try {
                    fileHost.insert(path, offset, data);
                    this.respondClientSuccess(receivingMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.respondClientError(receivingMessage);
                }
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
                try {
                    fileHost.delete(path, offset, length);
                    this.respondClientSuccess(receivingMessage);
                } catch (Exception e) {
                    this.respondClientError(receivingMessage);
                    e.printStackTrace();
                }
                break;
            }
            case "CHECK": {
                String path = receivingMessage.header.getParams()[1];
                boolean exist;
                try {
                    fileHost.get(path);
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
                    this.respondClient(receivingMessage, header, new EmptyDataLoader());
                } else {
                    this.respondClientError(receivingMessage);
                }
                break;
            }
        }
    }
}
