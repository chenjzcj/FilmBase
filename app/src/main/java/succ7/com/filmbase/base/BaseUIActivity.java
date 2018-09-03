package succ7.com.filmbase.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import succ7.com.filmbase.AppManager;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.listener.AoListener;
import succ7.com.filmbase.push.UmengPushHelper;
import succ7.com.filmbase.utils.StatusBarUtils;
import succ7.com.filmbase.view.BoundDragView;
import succ7.com.filmbase.view.TabLoadingProgressView;
import succ7.com.filmbase.view.TitleBarView;
import succ7.com.filmbase.view.dialog.DialogControl;
import succ7.com.filmbase.view.dialog.DialogHelper;
import succ7.com.filmbase.view.dialog.WaitDialog;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/16 0016
 * 最最基础的activity
 */
public class BaseUIActivity extends FragmentActivity implements DialogControl, VisibilityControl {

    protected BaseUIActivity mBaseActivity;
    public LayoutInflater mInflater;
    protected TitleBarView mTitleBar;
    protected LinearLayout mainview;
    protected LinearLayout subview;
    protected LinearLayout bar_shadow;
    public boolean isPushTodown;
    protected TabLoadingProgressView mprogress;
    protected BoundDragView mBoundDraView;
    private boolean _isVisible;
    private WaitDialog _waitDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.isPushTodown = false;
        //this.isPushTodown = true;
        this.mBaseActivity = this;
        setContentView(this.getLayoutId());

        this.mInflater = LayoutInflater.from(this.mBaseActivity);

        this.mTitleBar = (TitleBarView) this.findViewById(R.id.layout_titlebar);
        this.mainview = (LinearLayout) this.findViewById(R.id.layout_mainview);
        this.subview = (LinearLayout) this.findViewById(R.id.layout_subview);
        this.bar_shadow = (LinearLayout) this
                .findViewById(R.id.title_bar_shadow);
        this.mBoundDraView = (BoundDragView) this.findViewById(R.id.drag);
        if (this.mBoundDraView != null) {
            this.mBoundDraView.setVisibility(View.VISIBLE);
        }
        this.mprogress = (TabLoadingProgressView) this
                .findViewById(R.id.loading_bar);
        //设置沉浸式状态栏
        StatusBarUtils.setTranslucentStatus(true, this, 0);
        AppManager.getAppManager().addActivity(this);
        UmengPushHelper.onAppStart(this);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(int title) {
        this.mTitleBar.setTitle(title);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        this.mTitleBar.setTitle(title);
    }

    /**
     * 添加返回按钮监听
     *
     * @param listener
     */
    public void addBackListener(View.OnClickListener listener) {
        ImageView imageView = new ImageView(this.mBaseActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.mipmap.ic_back);
        this.mTitleBar.setLeftLayoutListener(imageView, listener);
    }

    /**
     * 添加右边按钮点击监听
     *
     * @param listener
     */
    public void addListener(View.OnClickListener listener) {
        ImageView imageView = new ImageView(this.mBaseActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.mipmap.ic_add);
        this.mTitleBar.setRightLayoutListener(imageView, listener);
    }

    /**
     * 添加主View
     *
     * @param view
     */
    public void addMainView(View view) {
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.weight = 1.0f;
        this.mainview.addView(view, linearLayout);
    }

    /**
     * 添加子view
     *
     * @param view
     */
    public void addSubView(View view) {
        LinearLayout.LayoutParams linearLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.weight = 1.0f;
        this.subview.addView(view, linearLayout);
    }

    /**
     * 设置是否显示阴影
     *
     * @param bool
     */
    public void setBarshadow(boolean bool) {
        if (bool) {
            this.bar_shadow.setVisibility(View.VISIBLE);
            return;
        }
        this.bar_shadow.setVisibility(View.GONE);
    }

    protected int getLayoutId() {
        return R.layout.layout_base;
    }

    /**
     * 添加返回监听
     */
    public void backListener() {
        this.addBackListener(new AoListener(this));
    }

    public void destoryActivity() {
    }

    /**
     * 显示加载提示
     */
    protected void showLoading() {
        mprogress.showLoading(getString(R.string.mj_loading));

    }

    /**
     * 隐藏加载提示
     */
    protected void hideProgress() {
        this.mprogress.hideLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    @Override
    public void onBackPressed() {
        if (this.isPushTodown) {
            destoryActivity();
            this.overridePendingTransition(0, R.anim.push_down);
            return;
        }
        destoryActivity();
        this.overridePendingTransition(0, R.anim.push_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    protected void onPause() {
        _isVisible = false;
        hideWaitDialog();
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        _isVisible = true;
        super.onResume();
        MobclickAgent.onResume(this);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
    }*/

    @Override
    protected void onStop() {
        super.onStop();
    }

    // ======================弹出框================================
    @Override
    public WaitDialog showWaitDialog() {
        return showWaitDialog(R.string.loading);
    }

    @Override
    public WaitDialog showWaitDialog(int resid) {
        return showWaitDialog(getString(resid));
    }

    @Override
    public WaitDialog showWaitDialog(String message) {
        if (_isVisible) {
            if (_waitDialog == null) {
                _waitDialog = DialogHelper.getWaitDialog(this, message);
            }
            if (_waitDialog != null) {
                _waitDialog.setMessage(message);
                _waitDialog.show();
            }
            return _waitDialog;
        }
        return null;
    }

    @Override
    public void hideWaitDialog() {
        if (_isVisible && _waitDialog != null) {
            try {
                _waitDialog.dismiss();
                _waitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean isVisible() {
        return _isVisible;
    }

    @Override
    public void setVisible(boolean visible) {
        _isVisible = visible;
    }


    public void hideSoftInputMethod() {

        final View v = this.getWindow().peekDecorView();
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void showSoftInputMethod(View focusView) {
        focusView.requestFocus();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }


    private List<ImageView> imaList = new ArrayList<ImageView>();
    private int[] guidResID;

    public int[] getGuidResID() {
        return guidResID;
    }

    public void setGuidResID(int[] guidResID) {
        this.guidResID = guidResID;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 子类看需求自行实现
     */
    protected void postClick() {
    }

    /**
     * 进入指定的activity
     *
     * @param activity Activity
     * @param cls      cls
     */
    public static void enterActivity(Activity activity, Class cls, boolean finish) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
        if (finish)
            activity.finish();
    }

    public static void postEvent(Object obj) {
        EventBus.getDefault().post(obj);
    }
}
