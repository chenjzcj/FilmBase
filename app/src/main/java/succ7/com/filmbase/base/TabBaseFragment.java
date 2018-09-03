package succ7.com.filmbase.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;

import de.greenrobot.event.EventBus;
import succ7.com.filmbase.R;
import succ7.com.filmbase.view.TabLoadingProgressView;
import succ7.com.filmbase.view.TitleBarView;
import succ7.com.filmbase.view.dialog.DialogHelper;
import succ7.com.filmbase.view.dialog.WaitDialog;

/**
 * Created by Administrator on 2015/5/22.
 * Fragment基类,包含公共的操作
 */
public abstract class TabBaseFragment extends Fragment {
    private WaitDialog waitDialog;
    protected TitleBarView titlebar;
    protected View barShadow;
    private View errorview;
    protected TabLoadingProgressView progress;
    protected ViewStub mViewStub;
    protected LayoutInflater layoutInflater;
    protected int h;
    protected boolean isShowTBar = true;
    public Context mContext;

    public TabBaseFragment() {
        this.h = 0;
    }

    public void setH(int h) {
        this.h = h;
    }

    /**
     * 是否显示标题栏
     *
     * @param isShowTBar 是否显示标题栏
     */
    public void showtitleBar(boolean isShowTBar) {
        this.isShowTBar = isShowTBar;
        if (this.titlebar == null) {
            return;
        }
        if (isShowTBar) {
            this.titlebar.setVisibility(View.VISIBLE);
            this.barShadow.setVisibility(View.VISIBLE);
        } else {
            this.titlebar.setVisibility(View.GONE);
            this.barShadow.setVisibility(View.GONE);
        }

    }

    /**
     * 显示加载失败view
     *
     * @param falg
     */
    public void showErrorView(boolean falg) {
        if (this.errorview == null) {
            return;
        }
        if (falg) {
            this.errorview.setVisibility(View.VISIBLE);
        } else {
            this.errorview.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化View
     */
    public abstract void fragmentView();

    public boolean hasView() {
        return this.mViewStub.getLayoutResource() <= 0;
    }

    public void loadFragmentView() {
        if (this.hasView()) {
            this.fragmentView();
        }
        this.hideProgress();
    }

    public void showFragmentView() {
        if (this.hasView()) {
            this.fragmentView();
        }
        this.hideProgress();
    }

    public void j() {
        this.hideProgress();
    }

    protected void setProgressTxt() {
        this.progress.showLoading(this.getString(R.string.mj_loading));
    }

    protected void showLoading() {
        progress.showLoading(getString(R.string.mj_loading));
    }

    public void hideProgress() {
        this.progress.hideLoading();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        layoutInflater = LayoutInflater.from(this.getActivity());
        mContext = this.getActivity();
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    //子类可以继承,也可以不继承
    public void onEventMainThread(Object obj) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_tab_fragment_base, container,
                false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.titlebar = (TitleBarView) view.findViewById(R.id.layout_titlebar);
        this.barShadow = view.findViewById(R.id.title_bar_shadow);
        this.errorview = view.findViewById(R.id.errorview);
        this.errorview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setVisibility(View.GONE);
                errorViewListener();
            }
        });
        this.showtitleBar(this.isShowTBar);
        progress = (TabLoadingProgressView) view.findViewById(R.id.loading_bar);
        showLoading();
        mViewStub = (ViewStub) view.findViewById(R.id.view_container);
    }

    /**
     * 点击加载失败的view回调的方法
     */
    public abstract void errorViewListener();

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.hideProgress();
        // 注销eventbus
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    public WaitDialog createWaitDialog(String msg) {
        if (waitDialog == null) {
            waitDialog = DialogHelper.getWaitDialog((Activity) mContext, msg);
        }
        waitDialog.setCancelable(true);
        return waitDialog;
    }

    public void hideWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    public static void postEvent(Object obj) {
        EventBus.getDefault().post(obj);
    }
}
