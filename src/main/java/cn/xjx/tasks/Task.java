package cn.xjx.tasks;

/**
 * Created by jiax on 2017/1/2.
 */
public class Task {
    private int taskNum;//任务编号
    private Node taskStart;//任务起点
    private Node taskEnd;//任务终点（针对移库任务）,如果是出入库任务，起点和终点相等,方便计算
    private boolean move;//是否是移库任务,默认为0出入库任务
    private boolean out;//是否是出库任务，0表示入库，1表示出库

    public Task() {}

    public Task(Node start, Node end, boolean isMove) {
        this.taskStart = start;
        this.taskEnd = end;
        this.move = isMove;
    }

    public Task(int taskNum, Node taskStart, Node taskEnd, boolean move, boolean out) {
        this.taskNum = taskNum;
        this.taskStart = taskStart;
        this.taskEnd = taskEnd;
        this.move = move;
        this.out = out;
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

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskNum=" + taskNum +
                ", taskStart=" + taskStart +
                ", taskEnd=" + taskEnd +
                ", move=" + move +
                ", out=" + out +
                '}';
    }
}
