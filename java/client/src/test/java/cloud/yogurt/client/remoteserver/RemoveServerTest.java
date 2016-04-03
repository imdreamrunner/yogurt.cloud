package cloud.yogurt.client.remoteserver;

import cloud.yogurt.client.servicecall.GetFileByPath;
import cloud.yogurt.client.servicecall.ServiceCall;
import cloud.yogurt.shared.network.PacketException;
import cloud.yogurt.shared.sharedconfig.SharedConfig;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class RemoveServerTest {
    @Test
    public void dummyServiceCall() throws IOException, PacketException {
        ServiceCall call = new GetFileByPath("test.txt");
        assertTrue(call.getMessageDataLoader().available() > 0);
    }

    @Test
    public void makeServiceCall() throws IOException, PacketException {
        ServiceCall call = new GetFileByPath("test.txt");
        RemoteServer server = new RemoteServer(SharedConfig.SERVER_ADDRESS, SharedConfig.SERVER_PORT);
        server.makeServiceCall(call);
    }
}
