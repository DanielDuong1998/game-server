package org.example.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameServerHandler1 extends ChannelInboundHandlerAdapter {
    private static final Map<String, ChannelHandlerContext> players = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("👤 Người chơi kết nối: " + ctx.channel().remoteAddress());
        players.put(ctx.channel().remoteAddress().toString(), ctx);
//        broadcast("UPDATE_POSITION " + ctx.channel().remoteAddress());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String message = (String) msg;
        System.out.println("📥 Nhận dữ liệu từ Client: " + message);

        if (message.startsWith("PLAYER_MOVE")) {
            // Ví dụ: "PLAYER_MOVE 100 200"
            String[] parts = message.split(" ");
            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);

            // Gửi tọa độ mới của người chơi đến tất cả client
            broadcast("UPDATE_POSITION " + ctx.channel().remoteAddress() + " " + x + " " + y);
        }
    }

    private void broadcast(String message) {
        for (ChannelHandlerContext ctx : players.values()) {
            ctx.writeAndFlush(message + "\n");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("❌ Người chơi rời game: " + ctx.channel().remoteAddress());
        players.remove(ctx.channel().remoteAddress().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof IOException) {
            System.out.println("Client đã ngắt kết nối: " + ctx.channel().remoteAddress());
        } else {
            cause.printStackTrace(); // In lỗi khác để debug
        }
        ctx.close();
    }
}