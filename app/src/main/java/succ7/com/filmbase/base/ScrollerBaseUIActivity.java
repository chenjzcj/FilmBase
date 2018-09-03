package succ7.com.filmbase.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import de.greenrobot.event.EventBus;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.listener.IAnimationListener;
import succ7.com.filmbase.view.BaseParentScrollView;
import succ7.com.filmbase.view.dialog.DialogHelper;
import succ7.com.filmbase.view.dialog.WaitDialog;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/17 0017
 * 滚动切换页面的UI基类
 */
public class ScrollerBaseUIActivity extends BaseUIActivity implements
        Handler.Callback, IAnimationListener {
    //ScrollerBaseUIActivity特有的view对象,其他的view的都在其父类BaseUIActivity中初始化过了,可以直接使用,只是多包了一层而已
    protected BaseParentScrollView parentScrollView;
    protected Handler mHandler;
    //加载对话框
    public WaitDialog waitDialog;

    public ScrollerBaseUIActivity() {
        this.parentScrollView = null;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        this.parentScrollView = (BaseParentScrollView) this.findViewById(R.id.parent_scrollview);
        parentScrollView.setOnAnimationListener(this);

        // 首先让屏幕不可以被拖拽
        this.parentScrollView.setDrag(false);
        //将拖拽边界view显示出来
        this.mBoundDraView.setVisibility(View.VISIBLE);

        // 800毫秒过后,设置让屏幕可以被拖拽
        this.mHandler = new Handler(this);
        mHandler.sendEmptyMessageDelayed(0, 800);
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    //子类可以继承,也可以不继承
    public void onEventMainThread(Object obj) {
    }

    /*****************
     * IAnimationListener接口的方法
     *********************/
    @Override
    public void d(boolean b) {

    }

    @Override
    public void e(boolean b) {
        if (!this.parentScrollView.i() && this.parentScrollView.a()) {
            this.a_(b);
        } else if (this.parentScrollView.i() && this.parentScrollView.a()) {
            this.f(b);
        }
    }

    /*****************
     * IAnimationListener接口的方法
     *********************/
    @Override
    protected int getLayoutId() {
        return R.layout.layout_hori_scroller_ui_base;
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 0: {
                this.isDrag(false);
                this.isDrag(true);
                break;
            }
        }
        return false;
    }

    /**
     * 设置parentScrollView是否可以被拖拽
     *
     * @param drag
     */
    public void isDrag(boolean drag) {
        this.mHandler.removeMessages(0);
        this.parentScrollView.setDrag(drag);
    }

    public void a_(boolean b) {
        this.finish();
    }

    public void f(boolean b) {

    }

    @Override
    public void backListener() {
        this.addBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.isPushTodown) {
            this.finish();
            this.overridePendingTransition(0, R.anim.push_down_acc);
            return;
        }
        this.finish();
        this.overridePendingTransition(0, R.anim.push_right_acc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mHandler.removeMessages(0);
        // 注销eventbus
        EventBus.getDefault().unregister(this);
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
}
