package succ7.com.filmbase.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import de.greenrobot.event.EventBus;
import succ7.com.filmbase.AppManager;
import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.HomeActivity;
import succ7.com.filmbase.push.UmengPushHelper;
import succ7.com.filmbase.utils.StatusBarUtils;
import succ7.com.filmbase.view.dialog.DialogHelper;
import succ7.com.filmbase.view.dialog.WaitDialog;

/**
 * 需要继承RoboActivity才可以使用
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/8 0008
 */
public class BaseActivity extends Activity {
    public Context mBaseContext;
    //加载对话框
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view注入初始化
        x.view().inject(this);
        //注册eventbus
        EventBus.getDefault().register(this);
        this.mBaseContext = this;
        //设置沉浸式状态栏
        StatusBarUtils.setTranslucentStatus(true, this, 0);
        AppManager.getAppManager().addActivity(this);
        UmengPushHelper.onAppStart(this);
    }

    /**
     * 初始化View
     */
    protected void initView() {
    }

    /**
     * 创建对话框对象
     *
     * @param msg 提示内容
     * @return
     */
    public WaitDialog creatWaitDialog(String msg) {
        waitDialog = DialogHelper.getWaitDialog(this, msg);
        waitDialog.setCancelable(true);
        return waitDialog;
    }

    /**
     * 隐藏对话框
     */
    public void hideWaitDialog() {
        if (waitDialog != null) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销eventbus
        EventBus.getDefault().unregister(this);
        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 必须要有这个方法,子类可以选择是否重写,否则需要在每个activity里面注册与注销eventbus
     *
     * @param obj
     */
    public void onEventMainThread(Object obj) {
    }

    @Override
    public void onBackPressed() {
        this.finish();
        this.overridePendingTransition(0, R.anim.push_right_acc);
    }

    /**
     * 进入主页面
     */
    protected void enterHome() {
        Intent intent = new Intent(mBaseContext, HomeActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
