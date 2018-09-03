package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.ShareUtils;

/**
 * Created by mzia on 2016/2/24.
 * 新闻详情页面
 */
public class WebActivity extends ScrollerBaseUIActivity {

    private static final String FILM = "film";
    private WebView webNews;
    private ProgressBar pb;
    private LinearLayout llPop;
    private Film film;

    public static void openWebNewsActivity(Activity activity, Film film) {
        Intent intent = new Intent(activity, WebActivity.class);
        intent.putExtra(FILM, film);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        film = (Film) intent.getSerializableExtra(FILM);
        this.mTitleBar.setTitle(film.getFilmName());
        backListener();
        addEditListener();
        View view = this.mInflater.inflate(R.layout.layout_webnews, null);
        this.addMainView(view);
        initView(view, film.getFilmAddress());
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

    private void initView(View view, String url) {
        //设置背景颜色 : http://blog.csdn.net/catoop/article/details/39667841
        pb = (ProgressBar) view.findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        webNews = (WebView) view.findViewById(R.id.webnews);
        llPop = (LinearLayout) view.findViewById(R.id.ll_pop);
        WebSettings settings = webNews.getSettings();
        settings.setSupportZoom(true);          //支持缩放
        settings.setBuiltInZoomControls(true);  //启用内置缩放装置
        settings.setJavaScriptEnabled(true);    //启用JS脚本
        webNews.setWebViewClient(new WebViewClient() {
            //当点击链接时,希望覆盖而不是打开新窗口
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);  //加载新的url
                return true;    //返回true,代表事件已处理,事件流到此终止
            }

        });
        //点击后退按钮,让WebView后退一页(也可以覆写Activity的onKeyDown方法)
        webNews.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (llPop.getVisibility() == View.VISIBLE) {
                        animShowOrHideView(llPop);
                        return true;
                    }
                    if (keyCode == KeyEvent.KEYCODE_BACK && webNews.canGoBack()) {
                        webNews.goBack();   //后退
                        return true;    //已处理
                    }
                }
                return false;
            }
        });

        webNews.setWebChromeClient(new WebChromeClient() {
            //当WebView进度改变时更新窗口进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //Activity的进度范围在0到10000之间,所以这里要乘以100
                WebActivity.this.setProgress(newProgress * 100);
                LogUtils.i("progress = " + newProgress);
                if (newProgress < 100) {
                    pb.setProgress(newProgress);
                } else {
                    pb.setVisibility(View.GONE);
                }
            }
        });
        webNews.loadUrl(url);
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
}
