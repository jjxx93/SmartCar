package cn.xjx;

import cn.xjx.tasks.Node;
import io.netty.channel.Channel;

/**
 * Created by jjxx9 on 2017/2/17.
 */
public class RobotChannel {
    Channel channel;

    Node coordinate;

    public RobotChannel(Channel channel) {
        this.channel = channel;
    }

    public RobotChannel(Channel channel, Node coordinate) {
        this.channel = channel;
        this.coordinate = coordinate;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Node getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Node coordinate) {
        this.coordinate = coordinate;
    }
}
