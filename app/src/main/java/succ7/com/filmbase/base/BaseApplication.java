package succ7.com.filmbase.base;

import android.content.Context;
import android.content.res.Resources;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.bugly.crashreport.CrashReport;

import org.litepal.LitePalApplication;
import org.xutils.x;

import java.io.File;

import succ7.com.filmbase.R;
import succ7.com.filmbase.utils.PathUtils;
import succ7.com.filmbase.utils.PhoneUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/8 0008
 * 应用程序基类
 */
public class BaseApplication extends LitePalApplication {
    public static Context mContext;
    public static Resources mResources;
    public static PhoneUtils phoneUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mResources = getResources();
        phoneUtils = new PhoneUtils(mContext);
        CrashReport.initCrashReport(getApplicationContext(), getString(R.string.bugly_appkey), false);
        initXuitls();
        initImageLoader();
    }

    /**
     * xutils初始化
     */
    private void initXuitls() {
        x.Ext.init(this);
        // 是否输出debug日志
        x.Ext.setDebug(false);
    }

    public static Context getmContext() {
        return mContext;
    }

    public static PhoneUtils getPhoneUtils() {
        return phoneUtils;
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) mContext;
    }

    private void initImageLoader() {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(BaseApplication.getContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                //.writeDebugLogs() // Remove for release app
                .discCache(new UnlimitedDiscCache(new File(PathUtils.generatePhotoCacheDir())))
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}
