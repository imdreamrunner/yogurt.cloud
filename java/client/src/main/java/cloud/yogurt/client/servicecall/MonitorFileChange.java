package cloud.yogurt.client.servicecall;

import cloud.yogurt.shared.header.Header;
import cloud.yogurt.shared.header.HeaderIntegerValue;
import cloud.yogurt.shared.header.HeaderRow;

import java.util.ArrayList;

/**
 * Service call to monitor the change of a file.
 */
public class MonitorFileChange extends ServiceCall {
    public MonitorFileChange(String filename, int duration) {
        super(new Header(
                new String[] {"MONITOR", filename},
                new ArrayList<HeaderRow>() {
                    {
                        add(new HeaderRow("Duration", new HeaderIntegerValue(duration)));
                    }
                }
        ));
    }
}
