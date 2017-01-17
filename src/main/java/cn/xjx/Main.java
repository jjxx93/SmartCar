package cn.xjx;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
    private static Logger logger = Logger.getLogger(Server.class);

    public static void main(String[] args) {
        //System.out.println("Hello");

        int port = 8090;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        System.out.println(System.getProperty("user.dir"));
        PropertyConfigurator.configure ("./log4j.properties");//读取使用Java的特性文件编写的配置文件。

        // 记录debug级别的信息
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        logger.error("This is error message.");

        // 启动按键命令处理
//        new KeyHandler().start();
//        // 启动服务器
//        new Server().bind(port);
        int i = 0;
        while(i++<100){
            System.out.println("/qiang");
        }
    }
}
