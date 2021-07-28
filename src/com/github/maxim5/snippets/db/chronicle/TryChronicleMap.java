package com.github.maxim5.snippets.db.chronicle;

import net.openhft.chronicle.map.ChronicleMap;

import java.io.IOException;
import java.nio.file.Path;

// https://github.com/OpenHFT/Chronicle-Map/issues/271
// --add-opens java.base/java.lang.reflect=ALL-UNNAMED
// --add-exports java.base/jdk.internal.ref=ALL-UNNAMED
// --add-exports java.base/sun.nio.ch=ALL-UNNAMED
//
// Also https://stackoverflow.com/questions/42667837/running-application-with-java-9-module-java-base-does-not-opens-java-io
public class TryChronicleMap {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("data/chronicle");
        ChronicleMap<String, String> map = ChronicleMap.of(String.class, String.class)
                .averageKeySize(8).averageValueSize(8)
                .entries(100)
                .createPersistedTo(path.resolve("string-string.data").toFile());

        System.out.println(map.get("foo"));
        System.out.println(map.put("foo", "baz"));

        // transaction:
        // https://github.com/JetBrains/xodus/blob/master/benchmarks/src/jmh/java/jetbrains/exodus/benchmark/chronicle/JMHChronicleMapTokyoCabinetBenchmarkBase.java

        // more:
        // https://github.com/OpenHFT/Chronicle-Map/blob/ea/docs/CM_Tutorial.adoc#chronicle-map-tutorial
    }
}
