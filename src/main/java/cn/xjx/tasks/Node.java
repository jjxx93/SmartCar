package cn.xjx.tasks;

/**
 * Created by jiax on 2017/1/2.
 */
public class Node {
    public double x;
    public double y;

    public Node(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
