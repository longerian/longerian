package me.longerian.abcandroid.datetimepicker;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.Pair;

public class GlobalFileMap {

    private static ArrayMap<Pair<String, String>, String>
            localFileMap = new ArrayMap<Pair<String, String>, String>();

    public static void putFilePath(String module, String name, String path) {
        Pair<String, String> key = Pair.create(module, name);
        synchronized (localFileMap) {
            localFileMap.put(key, path);
        }
    }

    public static String getFilePath(String module, String name) {
        Pair<String, String> key = Pair.create(module, name);
        synchronized (localFileMap) {
            return localFileMap.get(key);
        }
    }

    public static void clearFilePath(String module, String name) {
        Pair<String, String> key = Pair.create(module, name);
        synchronized (localFileMap) {
            localFileMap.remove(key);
        }
    }

}
