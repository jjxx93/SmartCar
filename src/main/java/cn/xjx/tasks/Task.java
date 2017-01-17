package cn.xjx.tasks;

/**
 * Created by jiax on 2017/1/2.
 */
public class Task {
    private int taskNum;//任务编号
    private Node taskStart;//任务起点
    private Node taskEnd;//任务终点（针对移库任务）,如果是出入库任务，起点和终点相等,方便计算
    private boolean isMoveLocation;//是否是移库任务,默认为0出入库任务
    private boolean isOut;//是否是出库任务，0表示入库，1表示出库

    public Task() {}

    public Task(Node start,Node end, boolean isMove) {
        taskStart = start;
        taskEnd = end;
        isMoveLocation = isMove;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public Node getTaskStart() {
        return taskStart;
    }

    public void setTaskStart(Node taskStart) {
        this.taskStart = taskStart;
    }

    public Node getTaskEnd() {
        return taskEnd;
    }

    public void setTaskEnd(Node taskEnd) {
        this.taskEnd = taskEnd;
    }

    public boolean isMoveLocation() {
        return isMoveLocation;
    }

    public void setMoveLocation(boolean moveLocation) {
        isMoveLocation = moveLocation;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNum=" + taskNum +
                ", taskStart=" + taskStart +
                ", taskEnd=" + taskEnd +
                ", isMoveLocation=" + isMoveLocation +
                ", isOut=" + isOut +
                '}';
    }
}
