package cn.xjx;

import cn.xjx.tasks.Allocation;
import io.netty.channel.group.ChannelGroup;

import java.util.Scanner;

/**
 * Created by jjxx9 on 2017/2/16.
 */
public class ServerKeyHandler extends Thread{
    private ChannelGroup channels;

    public ServerKeyHandler(ChannelGroup channels) {
        this.channels = channels;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)){
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();

//                switch (msg) {
//                    case "e" : System.exit(0); break;
//                    default:   break;
//                }

                // 退出服务器指令
                if (msg.equals("e") || msg.equalsIgnoreCase("exit")) {
                    System.exit(0);
                }

                if (msg.length() > 4 && msg.startsWith("alloc")) {
                    String[] strings = msg.split(" ");
                    int ioTaskSize = Integer.valueOf(strings[1]);
                    int mvTaskSize = Integer.valueOf(strings[2]);

                    Allocation allocation = new Allocation(2, ioTaskSize,mvTaskSize);
//                    Allocation allocation = new Allocation(channels.size(), Integer.valueOf(strings[1]),Integer.valueOf(strings[2]));
                    allocation.allocTask();
                    allocation.displayResult();
                }
            }
        }
    }
}
