package cloud.yogurt.client.filecache;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple client side cache for content of file.
 */
public class FileCache {

    private Map<String, byte[]> fileContent = new HashMap<>();
    private Map<String, Long> modifyTime = new HashMap<>();
    private Map<String, Long> syncTIme = new HashMap<>();

    public void updateCache(String file, byte[] data, long updateTime) {
        fileContent.put(file,  data);
    }

    public byte[] getCache(String file) {
        if (fileContent.get(file) != null)  {
            return fileContent.get(file);
        }
        return null;
    }

}
