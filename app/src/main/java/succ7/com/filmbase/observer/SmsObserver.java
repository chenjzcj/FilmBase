package succ7.com.filmbase.observer;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/1 0001 16:07
 * 短信观察者
 */
public class SmsObserver extends ContentObserver {

    private Context context;

    public SmsObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        /*LogUtils.i("SmsObserver", "短信有变化啦" + Thread.currentThread().getName());
        //如果当前是在登录页面,才执行下面的步骤
        if (!(AppUtils.isForeground(context, NewLoginActivity.class.getName()) ||
                AppUtils.isForeground(context, ModifyNumberActivity.class.getName()))) {
            return;
        }
        // 每当有新短信到来时，使用我们获取短消息的方法
        new Thread() {
            @Override
            public void run() {
                try {
                    SmsInfo smsInfo = SMSUtils.getNewestSms(context);
                    if (smsInfo == null) {
                        LogUtils.i("SmsObserver", "smsInfo == null");
                    } else {
                        LogUtils.i("SmsObserver", "新短信内容" + smsInfo.getSmsbody());
                        if (smsInfo.getSmsbody().contains("登录彩虹桥")) {
                            String code = MyTextUtils.getNumFromStr(smsInfo.getSmsbody());
                            EventBus.getDefault().post(new SMSEvent(code));
                        }
                    }
                } catch (Exception e) {
                    LogUtils.i("SmsObserver", e.toString());
                }
            }
        }.start();*/
    }
}
