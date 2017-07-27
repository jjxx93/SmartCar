package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Netty时间服务器客户端
 * Created by jiax on 2016/12/6.
 */
public class Server {
    private Logger logger = Logger.getLogger(Server.class);

    public Server() {
        PropertyConfigurator.configure ("./log4j.properties");
    }

    /**
     * 服务器端创建
     * @param port 监听端口
     * @param channelHandlerAdapter 服务处理类
     */
    public void bind (int port, final ChannelHandlerAdapter channelHandlerAdapter) {
//        配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();     // 用于接受客户端连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();   // 用于进行SocketChannel的网络读写

        try {
            ServerBootstrap b = new ServerBootstrap();          // Socket服务端的辅助启动类
            b.group(bossGroup, workerGroup)                     // 绑定线程池
                    .channel(NioServerSocketChannel.class)      // 绑定channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(channelHandlerAdapter);
                        }
                    })                                          // 绑定IO事件处理类
                    .option(ChannelOption.SO_BACKLOG, 1024)     // 等待接受连接和以接受连接最多是1024个
                    .option(ChannelOption.TCP_NODELAY, true)   //通过NoDelay,使消息立即发出去，不用等待到一定的数据量才发出去
                    .childOption(ChannelOption.SO_KEEPALIVE, true);     //保持长连接状态
//            绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();              // 绑定并启用监听端口，用于异步操作的通知回调
            f.channel().closeFuture().sync();                   // 阻塞，等待服务端链路关闭以后main函数才退出
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            退出，释放线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        TimeServerHandler timeServerHandler = new TimeServerHandler();

        // 启动按键命令处理线程
        //new ServerKeyHandler(RobotServerHandler.channels).start();

        // 启动服务器
        new Server().bind(8090, timeServerHandler);
    }
}
