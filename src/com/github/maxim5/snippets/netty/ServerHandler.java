package com.github.maxim5.snippets.netty;

import com.google.common.flogger.FluentLogger;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.logging.Level;

public class ServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final FluentLogger log = FluentLogger.forEnclosingClass();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        ByteBuf content = Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.write(response);
        ctx.flush();

        log.at(Level.INFO).log("%s %s: %s", request.method(), request.uri(), response.status());
    }
}
