package succ7.com.filmbase;

import android.content.Context;
import android.util.Log;

import java.net.ConnectException;
import java.nio.channels.UnresolvedAddressException;

import succ7.com.filmbase.utils.LogUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/1/29 0029
 * 未捕获的异常处理器
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    //单例引用，这里我们做成单例的，因为我们一个应用程序里面只需要一个UncaughtExceptionHandler实例
    private static CrashHandler instance;

    private CrashHandler() {
    }

    //同步方法，以免单例多线程环境下出现异常
    public synchronized static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    //初始化，把当前对象设置成UncaughtExceptionHandler处理器
    public void init(Context ctx) {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    //当有未处理(即未进行try catch)的异常发生时，就会来到这里。。
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        String msg = "uncaughtException\nthread->: " + thread + "\nname->: " + thread.getName()
                + "\nid->: " + thread.getId() + "\nexception->: " + ex;
        LogUtils.saveLog(msg,"CrashHandler");

        if (ex instanceof ConnectException) {
            LogUtils.e("ConnectException : 服务器出错了,请稍候再连接");
            //这个异常那就不用管了,不用关闭了,不用做任何处理
            //EventBus.getDefault().post(new UnCrashExceptionEvent());
        }
        if(ex instanceof UnresolvedAddressException){
            LogUtils.e("UnresolvedAddressException : 服务器出错了,请稍候再连接");
        }

        String threadName = thread.getName();
        if ("sub1".equals(threadName)) {
            Log.d("Sandy", "");
        } else if ("".equals("aa")) {
            //这里我们可以根据thread name来进行区别对待，同时，我们还可以把异常信息写入文件，以供后来分析。
            Log.d("Sandy", "");
        }
    }
}
