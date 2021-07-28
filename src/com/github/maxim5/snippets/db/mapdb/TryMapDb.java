package com.github.maxim5.snippets.db.mapdb;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.util.concurrent.ConcurrentMap;

public class TryMapDb {
    public static void main(String[] args) {
        try (DB db = DBMaker.fileDB("data/mapdb/map.data").make()) {
            ConcurrentMap<String, byte[]> map = db.hashMap("map", Serializer.STRING, Serializer.BYTE_ARRAY).createOrOpen();
            System.out.println(toString(map.put("key", "here".getBytes())));
            System.out.println(toString(map.get("key")));
        }
    }

    private static String toString(byte[] bytes) {
        return bytes != null ? new String(bytes) : null;
    }
}
