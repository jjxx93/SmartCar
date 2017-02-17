package cn.xjx;

import cn.xjx.tasks.Allocation;
import cn.xjx.tasks.Node;
import cn.xjx.tasks.Robot;
import cn.xjx.tasks.Task;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by jjxx9 on 2017/2/16.
 */
public class ServerKeyHandler extends Thread{
    private Map<Channel, Robot> channels;

    public ServerKeyHandler(Map channels) {
        this.channels = channels;
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)){
            while (scanner.hasNext()) {
                String msg = scanner.nextLine();

                switch (msg) {
                    // 退出程序
                    case "e" : {
                        System.out.println("Bye~ ^_^");
                        System.exit(0);
                        break;
                    }
                    case "a" : {                                 // 分配任务
                        try {
                            // 1.初始化出入库位置
                            System.out.println("Allocation Starting...");
                            System.out.println("Please enter the size of map:");
                            int MAX_XY = Integer.valueOf(scanner.nextLine());       // 地图大小
                            Node inNode  = new Node(0,MAX_XY/2);
                            Node outNode = new Node(MAX_XY,MAX_XY/2);

                            // 2.初始化机器人链表
                            int robotSize = channels.size();
                            LinkedList<Robot> robots = new LinkedList<>();
                            for(Channel channel:channels.keySet()) {
                                channel.writeAndFlush("Please report your position!");
                            }
                            Thread.sleep(20);       // 等待获取位置信息
//                            for(Channel channel:channels.keySet()) {
//                                robots.add(channels.get(channel));
//                            }

                             // 将机器人添加到链表中
                            robots.addAll(channels.values());
                            System.out.println(robots);

                            // 3.初始化待分配的任务链表
                            LinkedList<Task> unAllocTasks = new LinkedList<>();
                            System.out.println("There are " + robotSize + " robots now!");
                            System.out.println("Please enter the size of IO tasks:");
                            int ioTaskSize = Integer.valueOf(scanner.nextLine());   // io任务数
                            Task newTask;
                            int taskNo = 0;
                            System.out.println("Please enter the starting coordinates and ending coordinates :");
                            for (int i = 0; i < ioTaskSize; i++) {
                                String[] coordinates = scanner.nextLine().split(" ");
                                Node startNode = new Node(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));
                                Node endNode = new Node(Double.valueOf(coordinates[2]), Double.valueOf(coordinates[3]));
                                newTask = new Task(taskNo++, startNode, endNode, true, false);
                                unAllocTasks.add(newTask);
                            }

                            System.out.println("Please enter the size of move tasks:");
                            int mvTaskSize = Integer.valueOf(scanner.nextLine());
                            System.out.println("Please enter the starting coordinates and ending coordinates :");
                            for (int i = 0; i < mvTaskSize; i++) {
                                String[] coordinates = scanner.nextLine().split(" ");
                                Node startNode = new Node(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));

                                newTask = new Task(taskNo++, startNode, startNode, false, false);
                                unAllocTasks.add(newTask);
                            }

                            Allocation allocation = new Allocation(inNode, outNode, robots, unAllocTasks);
                            allocation.allocTask();     // 分配任务
                            allocation.displayResult();

                            // 将任务发送给机器人
                            // 任务格式：[TaskSize|0 x y|1 x1 y1 x2 y2|....]
                            for(Channel channel:channels.keySet()) {
                                LinkedList<Task> tasks = channels.get(channel).getRobotTasks();
                                StringBuilder response = new StringBuilder();
                                response.append(tasks.size()).append('|');
                                for (Task task:tasks) {
                                    if (!task.isMove()) {
                                        response.append(0).append(' ');
                                        response.append(task.getTaskStart().getX()).append(' ');
                                        response.append(task.getTaskStart().getY()).append('|');
                                    } else {
                                        response.append(1).append(' ');
                                        response.append(task.getTaskStart().getX()).append(' ');
                                        response.append(task.getTaskStart().getY()).append(' ');
                                        response.append(task.getTaskEnd().getX()).append(' ');
                                        response.append(task.getTaskEnd().getY()).append('|');
                                    }
                                }
                                response.deleteCharAt(response.length()-1);
                                response.append('\n');
                                ByteBuf resp = Unpooled.copiedBuffer(response.toString().getBytes());
                                channel.writeAndFlush(resp);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Enter error, allocation END!");
                            break;
                        }

                        break;
                    }
                    default: break;
                }

//                // 退出服务器指令
//                if (msg.equals("e") || msg.equalsIgnoreCase("exit")) {
//                    System.exit(0);
//                }
//
//                // 分配任务指令
//                if (msg.length() > 4 && msg.startsWith("alloc")) {
//                    String[] strings = msg.split(" ");
//                    int ioTaskSize = Integer.valueOf(strings[1]);
//                    int mvTaskSize = Integer.valueOf(strings[2]);
//
//                    Allocation allocation = new Allocation(2, ioTaskSize, mvTaskSize);
////                    Allocation allocation = new Allocation(channels.size(), Integer.valueOf(strings[1]),Integer.valueOf(strings[2]));
//                    allocation.allocTask();
//                    allocation.displayResult();
//                }
            }
        }
    }
}
