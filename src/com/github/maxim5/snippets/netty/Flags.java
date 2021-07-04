package com.github.maxim5.snippets.netty;

import com.google.common.flogger.FluentLogger;
import picocli.CommandLine;

import java.util.logging.Level;

@SuppressWarnings("unused")
@CommandLine.Command(name = "example", mixinStandardHelpOptions = true, version = "0.0")
public final class Flags implements Runnable {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();
    public static final Flags FLAGS = new Flags();

    @CommandLine.Option(names = {"-p", "--port"}, defaultValue = "8080", description = "HTTP port")
    private int port;
    public int port() { return port; }

    /* package */ void parseCommandLine(String[] args) {
        int exitCode = new CommandLine(this).execute(args);
        if (exitCode != 0) {
            log.at(Level.FINE).log("Failed to parse flags. Returning %d", exitCode);
            System.exit(exitCode);
        }
    }

    @Override
    public void run() {
        log.at(Level.FINE).log("Flags: port=%d", port);
    }

    private Flags() {
    }
}
