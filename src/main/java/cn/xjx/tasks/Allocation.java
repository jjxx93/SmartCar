package cn.xjx.tasks;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by jiax on 2017/1/2.
 */
public class Allocation {
    private int robotSize;              //机器人数量
    private int IOtaskSize;             //待分配出入库任务数量
    private int MVtaskSize;             //待分配移库任务数量
    private Node inNode;                //仓库入口
    private Node outNode;               //仓库出口
    private LinkedList<Robot> robots;         //机器人链表
    private LinkedList<Task> unAllocTasks;    //待分配的任务链表
    
    private final int MAX_XY = 1000;
    private final double a = 0.5;       //路程性能a和时间性能(1-a)的倾向参数

    public Allocation(){}

    // 初始化任务和机器人链表
    public Allocation(int robotsize, int Iotasksize, int Mvtasksize){
        //1.参数初始化
        robotSize  = robotsize;
        IOtaskSize = Iotasksize;
        MVtaskSize = Mvtasksize;

        robots = new LinkedList<>();
        unAllocTasks = new LinkedList<>();

        double[] xy = {75, 80, 850, 900};
        //2.初始化机器人链表
        for(int i = 1; i <= robotSize; i++)
        {
            //仓库地图坐标限制在0--MAX_XY
//            double x = Math.random()*MAX_XY;
//            double y = Math.random()*MAX_XY;
//            double x=990,y=990;
            double x = xy[i], y = xy[i];
            System.out.println("x=" + x + " y=" + y);
            Node robotC = new Node(x, y);
            Robot newRobot = new Robot(i, robotC);     //初始化每个机器人编号及坐标
            robots.add(newRobot);
        }

        //3.初始化待分配的任务链表
        int temNum = 0;

        int[] b = {1, 0, 1, 1, 0, 0};
        int i = 0;
        while(Iotasksize > 0 || Mvtasksize > 0)
        {
            //任务坐标
//            Node taskStart = new Node(Math.random()*MAX_XY, Math.random()*MAX_XY);
//            Node taskEnd   = new Node(Math.random()*MAX_XY, Math.random()*MAX_XY);
            Node taskStart = new Node(500,600);
            Node taskEnd   = new Node(600,500);

            //如果随机数小于0.5，产生出入库任务，否则产生移库任务
            //产生随机出入库和移库顺序的任务序列
            //if (Math.random() < 0.5) {
            if (b[i] != 1) {
                if(Mvtasksize > 0) {
                    //如果是出入库任务，起点和终点相等
                    Task newTask = new Task(taskStart, taskStart, false);
                    newTask.setTaskNum(temNum);
                    temNum++;

                    //如果随机数小于0.5的话，为出库任务，否则为入库任务
                    //newTask.setOut(Math.random() < 0.5);
                    newTask.setOut(b[i] == 1);
                    unAllocTasks.add(newTask);

                    System.out.println("MvtaskNum: " + Mvtasksize);
                    Mvtasksize--;
                }
            } else {
                if(Iotasksize > 0) {
                    Task newTask = new Task(taskStart, taskEnd, true);
                    newTask.setTaskNum(temNum);
                    temNum++;
                    newTask.setOut(false);

                    unAllocTasks.add(newTask);

                    System.out.println("IotaskNum: " + Iotasksize);
                    Iotasksize--;
                }
            }

            i++;//
        }

        //4.初始化出入库位置
        inNode  = new Node(0,MAX_XY/2);
        outNode = new Node(MAX_XY,MAX_XY/2);

        for(Robot robot:robots) {
            System.out.println(robot);
        }

        for (Task task:unAllocTasks) {
            System.out.println(task);
        }
    }

    public void allocTask() {
        while(!unAllocTasks.isEmpty()) {
            int unallocatTaskSize = unAllocTasks.size();
            
            //0.初始化标值列表数组
		    double[][] price = new double[robotSize][unallocatTaskSize];
            
            //1.计算出价标值列表
            //当前未分配的任务的数量
            Iterator<Robot> robotIter = robots.iterator();

            for (int i = 0; i < robotSize; i++) {
                Robot robotTemp = robotIter.next();
                //1.1 机器人任务链表的最后一个任务，接尾法
                // TODO 到底是取链表的头还是尾？？
                LinkedList<Task> robotTasks = robotTemp.getRobotTasks();
                Node taskStartNodeR, taskEndNodeR;
                boolean isMoveR;

                //1.2机器人的任务链表上有任务时，用机器人的任务链表的末端任务计算
                if(!robotTasks.isEmpty()) {
                    // TODO 到底是取链表的头还是尾？？
                    taskStartNodeR = robotTasks.getLast().getTaskStart();
                    taskEndNodeR   = robotTasks.getLast().getTaskEnd();
                    isMoveR        = robotTasks.getLast().isMoveLocation();
                } else {    //1.3当机器人的任务链表为空时，用机器人的位置坐标计算
                    taskStartNodeR = robotTemp.getRobotCoord();
                    taskEndNodeR   = taskStartNodeR;
                    isMoveR        = true;
                }

                //List<Task>::iterator taskIter=unAllocTasks.begin();
                Iterator<Task> taskIter = unAllocTasks.iterator();
                for (int j = 0; j < unallocatTaskSize; j++) {
                    Task taskTemp = taskIter.next();
                    // 1.3.1计算新任务出价标值
                    Node taskStartNode = taskTemp.getTaskStart();
                    Node taskEndNode   = taskTemp.getTaskEnd();
                    boolean isMove     = taskTemp.isMoveLocation();

                    //1.3.2如果新任务是移库任务，自身代价保持原值
                    double selfPriceD, relatedPriceD, priceD, priceT;
                    if(isMove) {
                        selfPriceD    = Math.abs(taskStartNode.x-taskEndNode.x)+Math.abs(taskStartNode.y-taskEndNode.y);
                    } else {    //当为出入库任务时，自身代价置为0
                        selfPriceD    = 0;
                    }
                    relatedPriceD = Math.abs(taskEndNodeR.x-taskStartNode.x)+Math.abs(taskEndNodeR.y-taskStartNode.y);

                    //目标性能为总路程最短
                    priceD = selfPriceD + relatedPriceD;
                    //目标性能为总时间最短
                    priceT = robotTemp.getTasksPriceD() + selfPriceD + relatedPriceD;
                    price[i][j] = a*priceD + (1-a)*priceT;

                    System.out.print("price[" + i + "][" + j + "]:" + price[i][j] + "  ");
                }
                System.out.println();
            }

            //2.开始竞标计算,获得分配的任务编号及对应机器人的编号
            double temprice = price[0][0];
            int robotNum = 0;
            int taskNum  = 0;
            for(int i = 0; i < robotSize; i++) {
                for(int j = 0; j < unallocatTaskSize; j++) {
                    if(temprice > price[i][j]) {
                        temprice = price[i][j];
                        robotNum = i;
                        taskNum  = j;
                    }
                }
            }
            System.out.println("robotNum=" + robotNum + "  taskNum=" + taskNum);

            //3.1 将刚才分配的任务添加到对应的机器人任务链表
            // TODO 到底是取链表的头还是尾？？
            Iterator<Task> taskIter = unAllocTasks.iterator();
            while(taskNum-- != 0) {taskIter.next();}
            Task newTask = taskIter.next();
            robotIter = robots.iterator();
            while(robotNum-- != 0) {robotIter.next();}
            // TODO push_back = push 从链表后面加
            Robot robotTemp = robotIter.next();
            robotTemp.getRobotTasks().add(newTask);

            //3.2 计算累加路程代价
            double newPriceD, selfPriceD, relatPriceD;
            //在机器人任务链表还为空时，以机器人的坐标位置计算
            Node backTaskEnd;
            if(robotTemp.getRobotTasks().isEmpty()) {
                backTaskEnd = robotTemp.getRobotCoord();
            } else {
                // TODO 啥意思？
                //Task backTask = *(--(robotIter->robotTasks.end()));
                Task backTask = robotTemp.getRobotTasks().getLast();
                backTaskEnd   = backTask.getTaskEnd();
            }

            Node newTaskStart = newTask.getTaskStart();
            Node newTaskEnd   = newTask.getTaskEnd();
            if(newTask.isMoveLocation()) {  //3.2.1移库任务
                selfPriceD  = Math.abs(newTaskEnd.x-newTaskStart.x)+Math.abs(newTaskEnd.y-newTaskStart.y);
                relatPriceD = Math.abs(newTaskStart.x-backTaskEnd.x)+Math.abs(newTaskStart.y-backTaskEnd.y);
                newPriceD   = selfPriceD+relatPriceD;
            } else {    //3.2.2入库任务
                if(!newTask.isOut()) {
                    selfPriceD  = 2*(Math.abs(newTaskStart.x-inNode.x)+Math.abs(newTaskStart.y-inNode.y));
                    relatPriceD = Math.abs(newTaskStart.x-backTaskEnd.x)+Math.abs(newTaskStart.y-backTaskEnd.y);
                    newPriceD   = selfPriceD+relatPriceD;
                } else {    //3.2.3出库任务
                    selfPriceD  = 2*(Math.abs(newTaskStart.x-outNode.x)+Math.abs(newTaskStart.y-outNode.y));
                    relatPriceD = Math.abs(newTaskStart.x-backTaskEnd.x)+Math.abs(newTaskStart.y-backTaskEnd.y);
                    newPriceD   = selfPriceD+relatPriceD;
                }
            }
            // TODO 测试使用迭代器添加是否成功
            robotTemp.setTasksPriceD(robotTemp.getTasksPriceD() + newPriceD);

            //3.2将刚才分配的任务从未分配任务链表中删除
            // TODO 测试remove迭代器是否成功
            unAllocTasks.remove(newTask);

            System.out.println();
        }

        System.out.println("Allocation is completed!");
        System.out.println();
    }

    void displayResult() {
        System.out.println("The allocation result are:");

        Iterator<Robot> robotIter = robots.iterator();
	    double[] totalDistace = new double[robotSize];
        double meanDistance=0, variance=0;

        for(int i = 0; i < robotSize; i++) {
            Robot robotTemp = robotIter.next();
            Node robotNode = robotTemp.getRobotCoord();
            System.out.println("The tasks of robot " + i + "[node(" + robotNode.x + "," + robotNode.y + ")] are: ");

            LinkedList<Task> tasks = robotTemp.getRobotTasks();
            //list<task>::iterator taskIter=tasks.begin();
            Iterator<Task> taskIter = tasks.iterator();
            for(int j = 0; j < tasks.size(); j++) {
                Task taskTemp = taskIter.next();
                Node taskStartNode = taskTemp.getTaskStart();
                Node taskEndNode = taskTemp.getTaskEnd();
                System.out.println("    " + taskTemp.getTaskNum() + "[isMove:" + taskTemp.isMoveLocation() +
                        ",isOut:" + taskTemp.isOut() + ",startNode(" + taskStartNode.getX() + "," +
                        taskStartNode.getY() + "),EndNode(" + taskEndNode.getX() + "," + taskEndNode.getY() + ")]; ");
            }
            System.out.println("The total Distance is:" + robotTemp.getTasksPriceD());

            totalDistace[i] = robotTemp.getTasksPriceD();
        }

        double sumDistance=0;
        for(int i = 0; i < robotSize; i++) {sumDistance+=totalDistace[i];}
        meanDistance = sumDistance/robotSize;
        double tempV=0;
        for(int i = 0; i < robotSize; i++)
            tempV += Math.pow(totalDistace[i]-meanDistance, 2.0);
        variance= Math.sqrt(tempV);
        System.out.println("SumDistance:" + sumDistance);
        System.out.println("MeanDistance:"+ meanDistance);
        System.out.println("Variance:"+ variance);
    }

    public static void main(String[] args) {
        Allocation allocation = new Allocation(3, 3,3);
        allocation.allocTask();
        allocation.displayResult();
    }
}
