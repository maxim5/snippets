package com.github.maxim5.snippets.process;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

// https://stackoverflow.com/questions/54686/how-to-get-a-list-of-current-open-windows-process-with-java
// https://stackoverflow.com/questions/1490869/how-to-get-vm-arguments-from-inside-of-java-application
//
// http://akuma.kohsuke.org/
// https://github.com/kohsuke/akuma/tree/master/src/main/java/com/sun/akuma
//
// https://dzone.com/articles/running-a-java-class-as-a-subprocess
// https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
public class ProcessUtil {
    public static void main(String[] args) {
        long pid = ProcessHandle.current().pid();
        System.out.println(pid);
        System.out.println(ProcessHandle.current().info());
        System.out.println(fullVMArguments());
        // ProcessHandle.allProcesses().forEach(process -> System.out.println(processDetails(process)));
    }

    private static String processDetails(ProcessHandle process) {
        return String.format("%8d %8s %10s %26s %-40s",
                             process.pid(),
                             text(process.parent().map(ProcessHandle::pid)),
                             text(process.info().user()),
                             text(process.info().startInstant()),
                             text(process.info().commandLine()));
    }

    private static String text(Optional<?> optional) {
        return optional.map(Object::toString).orElse("-");
    }

    static String fullVMArguments() {
        String name = javaVmName();
        return (contains(name, "Server") ? "-server "
                : contains(name, "Client") ? "-client " : "")
                + joinWithSpace(vmArguments());
    }

    static List<String> vmArguments() {
        return ManagementFactory.getRuntimeMXBean().getInputArguments();
    }

    static boolean contains(String s, String b) {
        return s != null && s.indexOf(b) >= 0;
    }

    static String javaVmName() {
        return System.getProperty("java.vm.name");
    }

    static String joinWithSpace(Collection<String> c) {
        return join(" ", c);
    }

    public static String join(String glue, Iterable<String> strings) {
        if (strings == null) return "";
        StringBuilder buf = new StringBuilder();
        Iterator<String> i = strings.iterator();
        if (i.hasNext()) {
            buf.append(i.next());
            while (i.hasNext())
                buf.append(glue).append(i.next());
        }
        return buf.toString();
    }
}
