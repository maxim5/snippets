package com.github.maxim5.snippets.netty;

import com.google.common.flogger.FluentLogger;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.logging.Level;

public class SimpleNettyServer {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    public void run() throws Exception {
        // Create the multithreaded event loops for the server
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // A helper class that simplifies server configuration
            ServerBootstrap httpBootstrap = new ServerBootstrap();

            // Configure the server
            httpBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerInitializer()) // <-- Our handler created here
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture httpChannel = httpBootstrap.bind(Flags.FLAGS.port()).sync();
            log.at(Level.INFO).log("Server running at %d", Flags.FLAGS.port());

            // Wait until server socket is closed
            httpChannel.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        Flags.FLAGS.parseCommandLine(args);
        logSystemProperties();
        new SimpleNettyServer().run();
    }

    private static void logSystemProperties() {
        System.out.println("Properties:");
        logProp("task");
        logProp("java.util.logging.config.file");
        // System.out.println(System.getProperties());
    }

    private static void logProp(String name) {
        System.out.println("  " + name + ": " + System.getProperty(name));
    }
}
