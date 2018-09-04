package succ7.com.filmbase.push;

import android.content.Context;


/**
 * Created by MZIA(527633405@qq.com) on 2016/8/25 0025 17:05
 * 友盟推送工具类
 */
public class UmengPushHelper {

    /**
     * 注册推送服务
     * 务必在工程的Application类的 onCreate() 方法中注册推送服务，无论推送是否开启都需要调用此方法
     *
     * @param context 上下文
     */
    public static void register(final Context context) {
        /*PushAgent mPushAgent = PushAgent.getInstance(context);
        dealWithCustomMessage(mPushAgent);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.i("onSuccess = " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.i("onFailure = " + s + s1);
            }
        });*/

    }

    /**
     * 处理自定义消息
     *
     * @param mPushAgent PushAgent
     */
    /*public static void dealWithCustomMessage(PushAgent mPushAgent) {
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            *//**
             * 自定义消息的回调方法
             * *//*
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        String apkUrl = msg.custom;
                        x.http().get(new RequestParams(apkUrl), new Callback.ProgressCallback<File>() {
                            @Override
                            public void onWaiting() {
                                LogUtils.i("onWaiting");
                            }

                            @Override
                            public void onStarted() {
                                LogUtils.i("onStarted");
                            }

                            @Override
                            public void onLoading(long total, long current, boolean isDownloading) {
                                LogUtils.i("onLoading" + total + "/" + current + isDownloading);
                            }

                            @Override
                            public void onSuccess(File result) {
                                LogUtils.i("onSuccess" + result.getAbsolutePath());
                                ApkUtils.installApk(context, result);
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                LogUtils.i("onError" + ex + isOnCallback);
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                LogUtils.i("onCancelled" + cex);
                            }

                            @Override
                            public void onFinished() {
                                LogUtils.i("onFinished");
                            }
                        });
                    }
                });
            }

        };
        mPushAgent.setMessageHandler(messageHandler);
    }*/

    /**
     * 统计应用启动数据
     * 在所有的Activity 的onCreate 方法或在应用的BaseActivity的onCreate方法中添加：
     *
     * @param context 上下文
     */
    public static void onAppStart(Context context) {
        /*PushAgent.getInstance(context).onAppStart();*/
    }

}
