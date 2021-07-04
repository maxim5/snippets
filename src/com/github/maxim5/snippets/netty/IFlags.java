package com.github.maxim5.snippets.netty;

import picocli.CommandLine;

@CommandLine.Command(name = "example", mixinStandardHelpOptions = true, version = "0.0")
public interface IFlags extends Runnable {
    @CommandLine.Option(names = {"-p", "--port"}, defaultValue = "777", description = "HTTP port")
    int port();

    private static void parseCommandLine(String[] args) {
        // Object instance = Proxy.newProxyInstance(IFlags.class.getClassLoader(), new Class<?>[]{IFlags.class}, (proxy, method, args1) -> null);
        // System.out.println(instance);

        CommandLine cmd = new CommandLine(IFlags.class);
        int exitCode = cmd.execute(args);
        if (exitCode != 0) {
            // log.at(Level.FINE).log("Failed to parse flags. Returning %d", exitCode);
            System.exit(exitCode);
        }

        Object userObject = cmd.getCommandSpec().userObject();
        assert userObject instanceof IFlags;
        IFlags flags = (IFlags) userObject;
        System.out.println(flags);
        System.out.println(flags.port());
    }

    public static void main(String[] args) {
        IFlags.parseCommandLine(args);
    }
}
