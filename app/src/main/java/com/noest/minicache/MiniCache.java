package com.noest.minicache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * you can cache byte[] here, with high io speed,
 * data is safe, it will not lost when crash
 */
public class MiniCache {
    /**
     * cache folder, cache file in {@code sCacheFolder}/minicache/*
     */
    private static String sCacheFolder;

    /**
     * same process share one minicach instance
     */
    private static Map<String, MiniCache> cacheMap = new HashMap<>();

    private MiniCache() {
    }

    /**
     * init cache folder, if not exist create it
     *
     * @return cache file path
     */
    public static String init(String cacheFolder) {
        File file = new File(cacheFolder);
        if (file.exists() && file.isFile()) {
            // folder path exist, and is a file
            return null;
        }
        File folder = new File(cacheFolder, "minicache");
        if (folder.exists() && folder.isFile()) {
            // folder path exist, and is a file
            return null;
        }
        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (!success) {
                // make cache folder failed
                return null;
            }
        }
        sCacheFolder = folder.getAbsolutePath();
        return sCacheFolder;
    }

    public static MiniCache getDefaultCache() {
        return getCache("default");
    }

    private MmapOperator mOperator;

    /**
     * create with id(file name)
     */
    public static MiniCache getCache(String id) {
        if (sCacheFolder == null) {
            throw new IllegalStateException("need call MiniCache.init() first");
        }
        if (cacheMap.containsKey(id)) {
            return cacheMap.get(id);
        }
        File cacheFile = new File(sCacheFolder, id);

        if (!cacheFile.exists()) {
            try {
                boolean success = cacheFile.createNewFile();
                if (!success) {
                    throw new IllegalStateException("create cache file failed , check permission or storage");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MiniCache miniCache = new MiniCache();
        miniCache.mOperator = new MmapOperator(cacheFile.getAbsolutePath());

        cacheMap.put(id, miniCache);
        return miniCache;
    }


    public void putString(String key, String value) {
        mOperator.put(key, value.getBytes());
    }

    public String getString(String key, String def) {
        byte[] bytes = mOperator.get(key);
        if (bytes == null) {
            return def;
        }
        return new String(bytes);
    }

    public void putInt(String key, int value) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(value).array();
        mOperator.put(key, bytes);
    }

    public int getInt(String key, int def) {
        byte[] bytes = mOperator.get(key);
        if (bytes == null || bytes.length != 4) {
            return def;
        }
        return ByteBuffer.wrap(bytes).getInt();
    }

    public void put(String key, byte[] data) {
        mOperator.put(key, data);
    }

    public byte[] get(String key) {
        return mOperator.get(key);
    }

    public Map<String, byte[]> getAll() {
        return mOperator.getAll();
    }
}
