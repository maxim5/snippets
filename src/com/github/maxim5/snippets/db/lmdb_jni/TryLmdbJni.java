package com.github.maxim5.snippets.db.lmdb_jni;

import org.fusesource.lmdbjni.*;

// --add-opens java.base/java.lang.reflect=ALL-UNNAMED
// --add-exports java.base/jdk.internal.ref=ALL-UNNAMED
// --add-exports java.base/sun.nio.ch=ALL-UNNAMED
public class TryLmdbJni {
    public static void main(String[] args) {
        // System.out.println(System.getProperty("sun.arch.data.model"));
        // System.out.println(System.getProperty("java.library.path"));
        // System.out.println(System.getProperty("library.lmdbjni.path"));

        try (Env env = new Env()) {
            env.open("data/lmdb-jni", Constants.NOSYNC | Constants.WRITEMAP);
            env.setMapSize(100);
            try (Database db = env.openDatabase("simple")) {
                /*
                byte[] key = toBytes("foo");
                byte[] value = toBytes("bar");

                try (Transaction txn = env.createWriteTransaction()) {
                    try (BufferCursor cursor = db.bufferCursor(txn)) {
                        cursor.keyWriteBytes(key);
                        cursor.valWriteBytes(value);
                        cursor.put();
                    }
                    txn.commit();
                }

                try (Transaction txn = env.createReadTransaction()) {
                    try (BufferCursor c = db.bufferCursor(txn)) {
                        c.keyWriteBytes(key);
                        c.seekKey();
                        System.out.println(toString(c.valBuffer().byteArray()));
                    }
                }
                */

                System.out.println(toString(db.get(toBytes("Tampa"))));
                System.out.println(toString(db.put(toBytes("Tampa"), toBytes("rocks!"))));

                // db.delete(toBytes("Tampa"));
            }
        }
    }

    private static byte[] toBytes(String s) {
        return s.getBytes();
    }

    private static String toString(byte[] bytes) {
        return bytes != null ? new String(bytes) : null;
    }
}
