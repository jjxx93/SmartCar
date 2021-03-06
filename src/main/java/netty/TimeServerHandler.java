package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * 时间服务处理类
 * Created by jjxx9 on 2017/2/14.
 */
@ChannelHandler.Sharable
public class TimeServerHandler extends ChannelHandlerAdapter{

    private int counter;

    // 接收到客户端信息后的处理函数
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String)msg;

        System.out.println("Receive " + ++counter + " orders: " + body);

        String replyMessage = "[IP" + ctx.channel().remoteAddress() + "]: ";
        replyMessage += "Time".equalsIgnoreCase(body)? new Date() : "BAD ORDER";
        replyMessage += "\n";

        ByteBuf resp = Unpooled.copiedBuffer(replyMessage.getBytes());
        ctx.write(resp);            // 把待发送的消息放到发送缓冲数组中
    }

     // 客户端消息读取完毕，channelRead()执行完成
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }   // 将待发送消息写入SocketChannel

    // 发生异常时的处理函数
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient:" + incoming.remoteAddress() + "异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);

        System.out.println(ctx.name() + "已连接");
        System.out.println("The client`s IP is " + ctx.channel().remoteAddress());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        System.out.println(ctx.name() + "已断开");
        System.out.println("The client`s IP is " + ctx.channel().remoteAddress());
    }
}
