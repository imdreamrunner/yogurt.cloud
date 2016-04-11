package cloud.yogurt.shared.header;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HeaderTest {
    Header header;

    @After
    public void prepareHeader() {
        String string = header.toString();
        Header after = Header.fromString(string);
        System.out.println("HEADER\n" + string + "========");
        assertEquals(string, after.toString());
    }

    @Test
    public void test1() {
        String params[] = {"GET", "test.txt"};
        List<HeaderRow> rows = new ArrayList<>();
        header = new Header(params, rows);
    }

    @Test
    public void test2() {
        String params[] = {"NOTICE"};
        List<HeaderRow> rows = new ArrayList<>();
        rows.add(new HeaderRow("abc", new HeaderStringValue("def")));
        rows.add(new HeaderRow("abc", new HeaderIntegerValue(123)));
        header = new Header(params, rows);
    }

    @Test
    public void test3() {
        String params[] = {"HEIHEIHEI"};
        List<HeaderRow> rows = new ArrayList<>();
        rows.add(new HeaderRow("a", new HeaderStringValue("def")));
        rows.add(new HeaderRow("b", new HeaderIntegerValue(-123)));
        header = new Header(params, rows);
    }
}
