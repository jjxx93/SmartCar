package nativeTest;

/**
 * Created by jiax on 2016/12/30.
 */
public class HelloNative {
    // 静态初始化代码块，保证虚拟机在第一次使用该类时就会装载库
    static {
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary( "HelloNative" );
    }

    // native关键字表示本地方法，提醒编译器该方法将在外部定义
    public static native void greeting();

    // 测试greeting()函数
    public static void main(String[] args) {
        greeting();
    }
}
