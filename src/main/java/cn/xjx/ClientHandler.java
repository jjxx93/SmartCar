package cn.xjx;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 * Created by jiax on 2016/12/6.
 */
public class ClientHandler extends ChannelHandlerAdapter {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final ByteBuf firstMessage;

    public ClientHandler() {
        byte[] req = "Time".getBytes();
        firstMessage = Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);
    }

    //    客户端和服务端TCP链路建立成功后，调用channelActive方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(firstMessage);
    }

    //    当服务器返回应答信息时调用
    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);             // 读取返回信息
        try {
            String body = new String(req, "UTF-8");
            System.out.println("Now is : " + body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
        logger.warning("Unexpected exception from downstream : " + cause.getMessage());
        ctx.close();
    }
}
