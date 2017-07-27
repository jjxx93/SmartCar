package cn.xjx;

import cn.xjx.tasks.Node;
import cn.xjx.tasks.Robot;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 机器人服务处理类
 * Created by jjxx9 on 2017/2/14.
 */
@ChannelHandler.Sharable
public class RobotServerHandler extends ChannelHandlerAdapter {
    // 记录当前channel
//    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static Map<Channel, Robot> channels = new HashMap<>();

    // 接收到客户端信息后的处理函数
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        try {
            String message = new String(req, "UTF-8");
            System.out.print(new Date() + "--" + ctx.name() + ": ");
            System.out.println(message);

            String response = "";
            switch (message.charAt(0)) {
                case 't' : {            // 获取时间指令
                    response = new Date().toString();
                    break;
                }
                case 'p' : {            // 报告坐标指令
                    String[] strings = message.split(" ");
                    // 获得位置x、y
                    if (strings[2].endsWith("\n")) {    // 去掉\n
                        strings[2] = strings[2].substring(0, strings[2].length()-1);
                    }
                    Node position = new Node(Double.valueOf(strings[1]), Double.valueOf(strings[2]));
                    channels.get(ctx.channel()).setRobotCoord(position);

                    System.out.print(new Date() + "--" + ctx.name() + ": ");
                    System.out.println("Robot position is " + channels.get(ctx.channel()).getRobotCoord());
                    break;
                }
                case 's' : {            // 获取测试数据指令
                    response = "[3|0 5 1|1 2 1 5 4|0 2 1]";
                    break;
                }
                default: {              // 错误指令
                    response = "Error command, " + message;
                    break;
                }
            }

            ByteBuf resp = Unpooled.copiedBuffer(response.getBytes());
            ctx.write(resp);            // 把待发送的消息放到发送缓冲数组中
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        System.out.print(new Date() + "--" + ctx.name() + ": ");
        System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        // 当出现异常就关闭连接
        cause.printStackTrace();
        // 移除channel
        channels.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);

        // 添加channel

        String ctxName = ctx.name();
        int robotNo = ctxName.charAt(ctxName.length()-1) - 48;  // 获取ctx编号
        Robot robot = new Robot(robotNo, null);
        channels.put(ctx.channel(), robot);

        System.out.print(new Date() + "--" + ctx.name() + ": ");
        System.out.println(ctx.channel().remoteAddress() + " Connect!!!");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);

        // 移除channel
        channels.remove(ctx.channel());

        System.out.print(new Date() + "--" + ctx.name() + ": ");
        System.out.println(ctx.channel().remoteAddress() + " Disconnect!!!");
    }
}
