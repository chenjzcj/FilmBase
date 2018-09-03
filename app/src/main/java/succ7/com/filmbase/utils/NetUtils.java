package succ7.com.filmbase.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import succ7.com.filmbase.R;


/**
 * 跟网络相关的工具类
 *
 * @author zhongcj
 * @time 2015-1-15 下午10:56:17
 */
public class NetUtils {
    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context Context
     * @return true为连接 false为未连接
     * @author zhongcj
     */
    public static boolean isNetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     *
     * @param context Context
     * @return true为wifi连接 false为非wifi连接
     * @author zhongcj
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        //先要进行网络连接判断,只有在连接网络的情况才能判断是否是wifi连接
        if (networkInfo == null) {
            ToastUtils.showShortToast(context, "网络没有连接");
            return false;
        } else {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }
    }

    /**
     * 判断wifi是否连接
     *
     * @param context Context
     * @return true为wifi连接 false为非wifi连接
     */
    public static boolean isWifiOpen(Context context) {
        boolean isWifiConnect = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // check the networkInfos numbers
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isWifiConnect = false;
                }
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    isWifiConnect = true;
                }
            }
        }
        return isWifiConnect;
    }

    /**
     * 打开设置或网络设置界面
     *
     * @param context      Context
     * @param isNetSetting true 打开网络设置界面 false打开设置界面
     * @author zhongcj
     */
    public static void openSetting(Context context, boolean isNetSetting) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
            if (isNetSetting) {
                intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
            } else {
                intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            }
        } else {
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }
        context.startActivity(intent);
    }

    /**
     * 如果无网络,提示用户Toast
     *
     * @param context 上下文
     * @return 网络连接状态
     */
    public static boolean prepareRequest(Context context) {
        if (!isNetConnected(context)) {
            ToastUtils.showShortToast(context, R.string.tip_no_internet);
            return false;
        } else {
            return true;
        }
    }

}
