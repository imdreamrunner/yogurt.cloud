package cloud.yogurt.server.filehost;

import cloud.yogurt.shared.sharedconfig.SharedConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FileResolverTest {
    String LINE_1 = "Line 1.";
    String LINE_2 = "Line 2.";

    @Before
    public void prepareFile() throws FileNotFoundException, UnsupportedEncodingException {
        String serverBasePath = SharedConfig.SERVER_BASE_PATH;
        File baseDirectory = new File(serverBasePath);
        if (!baseDirectory.exists()) {
            boolean result = baseDirectory.mkdir();
            assertTrue(result);
        }
        File textFile = new File(serverBasePath + "/test.txt");
        PrintWriter writer = new PrintWriter(textFile, String.valueOf(SharedConfig.HEADER_CHARSET));
        writer.println(LINE_1);
        writer.println(LINE_2);
        writer.close();
    }

    @Test
    public void readWholeFile() throws IOException {
        int maxFileSize = 1000;
        byte[] buffer = new byte[maxFileSize];
        FileResolver fileResolver = new FileResolver("test.txt");
        int fileSize = fileResolver.read(buffer);
        byte[] stringInBytes = new byte[fileSize];
        System.arraycopy(buffer, 0, stringInBytes, 0, fileSize);
        assertEquals(new String(stringInBytes, SharedConfig.HEADER_CHARSET), LINE_1 + "\n" + LINE_2 + "\n");
    }
}
