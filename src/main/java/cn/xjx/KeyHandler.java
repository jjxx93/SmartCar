package cn.xjx;

import java.util.Scanner;

/**
 * 按键命令处理
 * Created by jiax on 2016/12/9.
 */
public class KeyHandler extends Thread{
    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)){
            if (scanner.nextLine().equals("e")) {
                System.exit(0);
            }
        }
    }
}

