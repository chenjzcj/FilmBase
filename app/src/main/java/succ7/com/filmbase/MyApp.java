package succ7.com.filmbase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import java.util.ArrayList;

import cn.bmob.statistics.AppStat;
import succ7.com.filmbase.base.BaseApplication;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.bmob.RealTimeHelper;
import succ7.com.filmbase.litepal.LitePalHelper;
import succ7.com.filmbase.observer.SmsObserver;
import succ7.com.filmbase.push.UmengPushHelper;
import succ7.com.filmbase.utils.SMSUtils;
import succ7.com.filmbase.utils.ShareUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/8 0008
 * 应用入口
 */
public class MyApp extends BaseApplication {

    private static MyApp instance;
    private ArrayList<Activity> activities = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //创建数据库
        LitePalHelper.createDB();
        //捕获未处理异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        //registerSMSObserver();
        BmobHelper.init(this);
        RealTimeHelper.start();
        ShareUtils.init();
        UmengPushHelper.register(instance);
        statInit();
    }

    /**
     * bmob统计
     */
    private void statInit() {
        /**
         * 调用统计SDK
         *
         * @param appKey
         *            Bmob平台的Application ID
         * @param channel
         *            当前包所在渠道，可以为空
         * @return 是否成功，如果失败请看logcat，可能是混淆或so文件未正确配置
         */

        AppStat.i("b98522cb5cecb387293166ffd7876fa2", "BMOB_CHANNEL");
    }

    /**
     * 注册短信观察者
     */
    private void registerSMSObserver() {
        getContentResolver().registerContentObserver(SMSUtils.URL_SMS, true, new SmsObserver(new Handler(), this));
    }

    /**
     * 获得当前app运行的MyApp
     *
     * @return instance
     */
    public static MyApp getInstance() {
        return instance;
    }

    /**
     * @param activity 添加Activity到容器中
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * @param activity 删除activity
     */
    public void deleteActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 退出activity
     */
    public void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
        activities.clear();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null) {
            info = new PackageInfo();
        }
        return info;
    }

    private static final String KEY_OFFLINE_MAP_FLAG = "offline_map_flag";
    private static String PREF_NAME = "creativelockerV2.pref";
    private static boolean sIsAtLeastGB;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            sIsAtLeastGB = true;
        }
    }


    public static SharedPreferences getPreferences() {
        return context().getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);
    }

    public static boolean getOfflineMapFalg() {
        return getPreferences().getBoolean(KEY_OFFLINE_MAP_FLAG, false);
    }

    public static void setOfflineMapFalg(boolean falg) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(KEY_OFFLINE_MAP_FLAG, falg);
        apply(editor);
    }

    public static void apply(SharedPreferences.Editor editor) {
        if (sIsAtLeastGB) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
