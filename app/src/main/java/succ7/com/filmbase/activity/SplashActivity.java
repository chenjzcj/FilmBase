package succ7.com.filmbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import succ7.com.filmbase.MyApp;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.BaseActivity;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.utils.StatusBarUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/15 0015
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(this, R.layout.activity_splash, null);
        setContentView(view);
        //设置沉浸式状态栏
        StatusBarUtils.setTranslucentStatus(true, this, R.color.white);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                nextOperation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    /**
     * 启动页面后续的操作
     */
    private void nextOperation() {
        Intent intent;
        //判断是否已经登录过了,如果没有登录或者注册,先注册或者登录
        if (MyApp.getPreferences().getBoolean(GuideActivity.ISFIRST, false)) {
            if (UserHelper.getCurrentUser() == null) {
                intent = new Intent(mBaseContext, LoginActivity.class);
            } else {
                intent = new Intent(mBaseContext, HomeActivity.class);
            }
        } else {
            //第一次安装使用,进入引导页
            intent = new Intent(SplashActivity.this, GuideActivity.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_acc, 0);
        finish();
    }

    @Override
    public void onBackPressed() {
        //防止误点返回键而退出应用
    }
}
