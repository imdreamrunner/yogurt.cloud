package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;
import cloud.yogurt.shared.logging.Logger;
import cloud.yogurt.shared.sharedconfig.SharedConfig;

import java.util.ArrayList;

public class InsertFragment extends ServiceCall {

    private static Logger log = Logger.getLogger(InsertFragment.class.getName());

    public InsertFragment(String path, int offset, String fragment) {
        super(
                new Header(new String[] {"INSERT", path}, new ArrayList<HeaderRow>() {
                    {
                        add(new HeaderRow("Offset", new HeaderIntegerValue(offset)));
                    }
                }),
                fragment.getBytes(SharedConfig.CONTENT_CHARSET)
        );
    }

}
