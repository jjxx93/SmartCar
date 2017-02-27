package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by jiax on 2016/12/9.
 */
public class Client {
    public void connect (int port, String host) {
//        配置NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();     // 用于处理IO读写

        try {
            Bootstrap bootstrap = new Bootstrap();                  // 客户端启动辅助类
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new ClientHandler());
                        }
                    });  // 将ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件

            // 发起异步连接操作
            ChannelFuture f = bootstrap.connect(host, port).sync();

            //Channel channel = f.channel();

            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8090;
//        if (args != null && args.length > 0) {
//            port = Integer.valueOf(args[0]);
//        }
        String serverIp1 = "219.223.240.87";
        String serverIp2 = "192.168.1.109";
        String localHost = "127.0.0.1";

        new Client().connect(port, localHost);
    }
}
