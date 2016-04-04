package cloud.yogurt.server.dulplicatefilter;

import cloud.yogurt.shared.network.EndPoint;
import cloud.yogurt.shared.network.EndPointCall;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to filter duplicate service call from the same client.
 */
public class DuplicateFilter {
    private Map<EndPointCall, Long> cache = new HashMap<>();

    public void mark(EndPointCall endPointCall) {
        long time = System.currentTimeMillis() / 1000L;
        cache.put(endPointCall, time);
    }

    public boolean isDuplicate(EndPointCall endPointCall) {
        return cache.get(endPointCall) != null;
    }
}
