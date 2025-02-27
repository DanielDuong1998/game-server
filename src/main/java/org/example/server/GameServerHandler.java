package org.example.server;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.io.IOException;

public class GameServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        System.out.println("Received from client: " + msg);
        // Gửi phản hồi (tùy chọn)
        ctx.writeAndFlush("Server received: " + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();

        if (cause instanceof IOException) {
            System.out.println("Client đã ngắt kết nối: " + ctx.channel().remoteAddress());
        } else {
            cause.printStackTrace(); // In lỗi khác để debug
        }
        ctx.close();
    }
}