package cloud.yogurt.shared.message;

import cloud.yogurt.shared.header.Header;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HeaderDataLoaderTest {
    @Test
    public void test1() throws IOException {
        HeaderDataLoader loader = new HeaderDataLoader(
                new Header(new String[]{"GET", "test.txt"}, new ArrayList<>())
        );

        byte[] buffer = new byte[1000];
        int read = loader.read(buffer);
        assertEquals(read, 36);
    }
}
