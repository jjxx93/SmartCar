package cn.xjx.tasks;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiax on 2017/1/2.
 */
public class Robot {
    private int robotNum;           //机器人编号
    private Node robotCoord;
    private LinkedList<Task> robotTasks;  //每个机器人对应的已分配到它身上的任务链表
    private double tasksPriceD;     //任务链表的总路程代价
    private Task tasking;           //机器人正在执行的任务

    public Robot(){}

    public Robot(int robotN,Node robotC) {
        robotNum = robotN;
        robotCoord = robotC;
        tasksPriceD = 0;
        robotTasks = new LinkedList<>();    // 使用链表实现数组
    }

    /**
     * 在任务序列的location位置插入新任务,
     * 机器人任务链表的第一个任务代表机器人当前正在执行的任务,所以插入的位置location从1开始
    **/
    void inserTask(int location,Task newTask) {
        robotTasks.add(location, newTask);
    }

    /**
     * 在任务序列的location位置取出任务,放到正在执行的tasking变量任务里
     * 当执行完链表里的头任务时，删除第一个任务
    **/
    void eraseTask(int location) {
        tasking = robotTasks.remove(location);
    }

    public int getRobotNum() {
        return robotNum;
    }

    public void setRobotNum(int robotNum) {
        this.robotNum = robotNum;
    }

    public Node getRobotCoord() {
        return robotCoord;
    }

    public void setRobotCoord(Node robotCoord) {
        this.robotCoord = robotCoord;
    }

    public LinkedList<Task> getRobotTasks() {
        return robotTasks;
    }

    public void setRobotTasks(LinkedList<Task> robotTasks) {
        this.robotTasks = robotTasks;
    }

    public double getTasksPriceD() {
        return tasksPriceD;
    }

    public void setTasksPriceD(double tasksPriceD) {
        this.tasksPriceD = tasksPriceD;
    }

    public Task getTasking() {
        return tasking;
    }

    public void setTasking(Task tasking) {
        this.tasking = tasking;
    }

    @Override
    public String toString() {
        return "Robot{" +
                "robotNum=" + robotNum +
                ", robotCoord=" + robotCoord +
                ", robotTasks=" + robotTasks +
                ", tasksPriceD=" + tasksPriceD +
                ", tasking=" + tasking +
                '}';
    }
}
