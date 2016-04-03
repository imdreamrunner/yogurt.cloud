package cloud.yogurt.shared.network;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;

public class EndPointCallTest {
    @Test
    public void test() throws UnknownHostException {
        EndPoint epA = new EndPoint();
        epA.address = InetAddress.getByName("127.0.0.1");
        epA.port = 12;
        EndPointCall cA = new EndPointCall(epA, 4);
        EndPoint epB = new EndPoint();
        epB.address = InetAddress.getByName("127.0.0.1");
        epB.port = 12;
        EndPointCall cB = new EndPointCall(epB, 4);
        assertEquals(cA, cB);
    }
}
