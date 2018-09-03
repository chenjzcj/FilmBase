package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.ShareUtils;

/**
 * http://download.csdn.net/download/lygscg123/7673123
 * http://blog.csdn.net/lygscg123/article/details/38084769
 * 推荐 : http://www.cnblogs.com/xianglinglong/p/5305627.html
 * http://www.bkjia.com/Androidjc/885842.html
 *
 * @author tanwenwei
 */
public class XWebViewActivity extends ScrollerBaseUIActivity {

    private FrameLayout videoview;// 全屏时视频加载view
    private WebView videowebview;
    private ProgressBar pb;
    private LinearLayout llPop;

    private View xCustomView;
    private xWebChromeClient xwebchromeclient;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    private static final String FILM = "film";
    private Film film;
    private LinearLayout llMainView;

    public static void openXWebViewActivity(Activity activity, Film film) {
        Intent intent = new Intent(activity, XWebViewActivity.class);
        intent.putExtra(FILM, film);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        film = (Film) intent.getSerializableExtra(FILM);
        setTitle(film.getFilmName());
        backListener();
        addEditListener();
        View view = this.mInflater.inflate(R.layout.activity_x_webview, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        llPop = (LinearLayout) view.findViewById(R.id.ll_pop);
        videoview = (FrameLayout) view.findViewById(R.id.video_view);
        llMainView = (LinearLayout) view.findViewById(R.id.ll_main_view);
        videowebview = (WebView) view.findViewById(R.id.video_webview);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        initwidget();
        videowebview.loadUrl(film.getFilmAddress());
    }

    /**
     * 添加编辑监听
     */
    private void addEditListener() {
        ImageView imageView = new ImageView(mBaseActivity);
        imageView.setImageResource(R.mipmap.more_v);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        mTitleBar.setRightLayoutListener(imageView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animShowOrHideView(llPop);
            }
        });
    }

    /**
     * 动画显示或者隐藏控件
     */
    private void animShowOrHideView(View view) {
        boolean isGone = view.getVisibility() == View.GONE;
        view.setVisibility(isGone ? View.VISIBLE : View.GONE);
        view.startAnimation(AnimationUtils.loadAnimation(mBaseActivity, isGone ? R.anim.popup_enter : R.anim.popup_exit));
    }

    private void initwidget() {
        WebSettings ws = videowebview.getSettings();
        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        videowebview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (inCustomView()) {
                        hideCustomView();
                        return true;
                    }
                    if (llPop.getVisibility() == View.VISIBLE) {
                        animShowOrHideView(llPop);
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK && videowebview.canGoBack()) {
                        videowebview.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });
    }

    /**
     * 判断是否是全屏
     *
     * @return true即为全屏状态
     */
    public boolean inCustomView() {
        return xCustomView != null;
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }

    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
            LogUtils.i("onShowCustomView");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            llMainView.setVisibility(View.GONE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
            XWebViewActivity.this.mTitleBar.setVisibility(View.GONE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;
            // Hide the custom view.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            videowebview.setVisibility(View.VISIBLE);
            llMainView.setVisibility(View.VISIBLE);
            XWebViewActivity.this.mTitleBar.setVisibility(View.VISIBLE);
            // Log.i(LOGTAG, "set it to webVew");
        }

        // 视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            // Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            }
            return xdefaltvideo;
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            // Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(XWebViewActivity.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        // 网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
        }

        @Override
        //当WebView进度改变时更新窗口进度
        public void onProgressChanged(WebView view, int newProgress) {
            //Activity的进度范围在0到10000之间,所以这里要乘以100
            XWebViewActivity.this.setProgress(newProgress * 100);
            LogUtils.i("progress = " + newProgress);
            if (newProgress < 100) {
                pb.setProgress(newProgress);
            } else {
                pb.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 处理各种通知、请求等事件
     */
    public class xWebViewClientent extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
            view.loadUrl(url);  //加载新的url
            return true;    //返回true,代表事件已处理,事件流到此终止
        }
    }


    /**
     * 分享给朋友
     *
     * @param view View
     */
    public void shareToFriend(View view) {
        animShowOrHideView(llPop);
        ShareUtils.shareFilm(this, film);
    }

    /**
     * 用浏览器打开
     *
     * @param view View
     */
    public void openInBrowser(View view) {
        animShowOrHideView(llPop);
        Uri uri = Uri.parse(film.getFilmAddress());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (videowebview != null) {
            videowebview.loadUrl("about:blank");
            videowebview.stopLoading();
            videowebview.setWebChromeClient(null);
            videowebview.setWebViewClient(null);
            videowebview.destroy();
            videowebview = null;
        }
        super.onDestroy();


    }
}
