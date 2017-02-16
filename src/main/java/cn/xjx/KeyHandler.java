package cn.xjx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.Scanner;

/**
 * 按键命令处理
 * Created by jiax on 2016/12/9.
 */
public class KeyHandler extends Thread{
    ChannelHandlerContext ctx;

    public KeyHandler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)){
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();

                if (msg.equals("e")) {
                    if (ctx != null) {
                        ctx.channel().close();
                        ctx.close();
                    }

                    System.exit(0);
                }

                if (ctx != null) {
                    byte[] req1 = msg.getBytes();
                    ByteBuf message = Unpooled.buffer(req1.length);
                    message.writeBytes(req1);
                    ctx.writeAndFlush(message);
                }
            }
        }
    }
}

