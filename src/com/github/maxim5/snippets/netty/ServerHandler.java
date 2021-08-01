package com.github.maxim5.snippets.netty;

import com.google.common.flogger.FluentLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    @Override
    protected void channelRead0(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) {
        handleResponse(ctx, request);
    }

    private void handleResponse(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) {
        FullHttpResponse response = newResponse();
        complete(ctx, request, response);
    }

    private void handlePromise(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) {
        Promise<FullHttpResponse> promise = getPromise(ctx);
        promise.addListener(future -> complete(ctx, request, (FullHttpResponse) future.getNow()));
    }

    private Promise<FullHttpResponse> getPromise(@NotNull ChannelHandlerContext ctx) {
        FullHttpResponse response = newResponse();
        return new DefaultPromise<FullHttpResponse>(ctx.channel().eventLoop()).setSuccess(response);
    }

    @NotNull
    private static FullHttpResponse newResponse() {
        ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return response;
    }

    private void complete(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request, FullHttpResponse response) {
        ctx.write(response);
        ctx.flush();
        // ctx.close();
        log.at(Level.INFO).log("%s %s: %s", request.method(), request.uri(), response.status());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.at(Level.WARNING).withCause(cause).log("Exception caught: %s", cause.getMessage());
    }
}
