package cloud.yogurt.client.filecache;

import cloud.yogurt.shared.logging.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple client side cache for content of file.
 */
public class FileCache {
    private static Logger log = Logger.getLogger(FileCache.class.getName());

    private Map<String, byte[]> fileContent = new HashMap<>();
    private Map<String, Long> modifyTime = new HashMap<>();
    private Map<String, Long> syncTime = new HashMap<>();

    public void updateCache(String file, byte[] data, long updateTime) {
        long currentTime = System.currentTimeMillis() / 1000;

        fileContent.put(file, data);
        modifyTime.put(file, updateTime);
        syncTime.put(file, currentTime);

        log.info("File " + file + "'s cache has been updated. " +
                "Last Update Time: " + updateTime + " " +
                "Sync Time: " + currentTime);
    }

    public void refreshCache(String file) {
        long currentTime = System.currentTimeMillis() / 1000;
        syncTime.put(file, currentTime);
    }

    public long getModifyTime(String file) {
        return modifyTime.get(file);
    }

    public byte[] getCache(String file, int duration) {
        long currentTime = System.currentTimeMillis() / 1000;
        if (fileContent.get(file) != null)  {
            long passed = currentTime - syncTime.get(file);
            if (passed > duration) {
                log.info("TIMEOUT",
                        "Cache was synced " + passed + "s ago.");
                return null;
            }
            log.info("CACHE_HIT", file + " is found in cache. Passed=" + passed);
            return fileContent.get(file);
        }
        return null;
    }

    public boolean hasCache(String file) {
        return fileContent.get(file) != null;
    }

    public void timeoutCache(String file) {
        fileContent.remove(file);
        modifyTime.remove(file);
        syncTime.remove(file);
    }

}
