package cn.xjx;

import com.alibaba.fastjson.JSONObject;
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

                String response = "";

                switch (msg.charAt(0)) {
                    // 退出程序命令
                    case 'e': {
                        ctx.channel().close();
                        ctx.close();
                        System.exit(0);
                    }

                    default: {
                        response = msg;
                        break;
                    }
                }

                ByteBuf resp = Unpooled.copiedBuffer(response.getBytes());
                ctx.writeAndFlush(resp);
            }
        }
    }
}

