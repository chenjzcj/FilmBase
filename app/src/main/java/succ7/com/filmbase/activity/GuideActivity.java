package succ7.com.filmbase.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import succ7.com.filmbase.MyApp;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.GuidePageAdapter;
import succ7.com.filmbase.base.BaseActivity;
import succ7.com.filmbase.utils.StatusBarUtils;
import succ7.com.filmbase.view.PagePointlView;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/15 0015
 * 引导展示页
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener {
    private PagePointlView mPagePoint;
    ViewPager mViewPage;
    private LinearLayout mOperateLayout;
    Button mBtnOk;
    List<View> mList = new ArrayList<>();
    public static final String ISFIRST = "isFirst";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPage = (ViewPager) findViewById(R.id.viewpager);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.guide1);

        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.mipmap.guide2);

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.mipmap.guide3);

        ImageView imageView3 = new ImageView(this);
        imageView3.setImageResource(R.mipmap.guide4);

        mList.add(imageView);
        mList.add(imageView1);
        mList.add(imageView2);
        mList.add(imageView3);

        mViewPage.setAdapter(new GuidePageAdapter(mList));
        mViewPage.addOnPageChangeListener(pageChangeListener);
        mPagePoint = (PagePointlView) findViewById(R.id.pageControl);
        mOperateLayout = (LinearLayout) findViewById(R.id.operate_layout);
        mViewPage.setCurrentItem(0);
        mPagePoint.setCount(mList.size());
        mPagePoint.setIndex(0);
        mBtnOk = (Button) findViewById(R.id.btnOK);
        mBtnOk.setOnClickListener(this);
        //设置沉浸式状态栏
        StatusBarUtils.setTranslucentStatus(true, this, R.color.white);
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mPagePoint.setIndex(position);
            if (position == 0) {
                mPagePoint.setVisibility(View.VISIBLE);
                mOperateLayout.setAnimation(null);
                mOperateLayout.setVisibility(View.GONE);
            } else if (position == 1) {
                mPagePoint.setVisibility(View.VISIBLE);
                mOperateLayout.setAnimation(null);
                mOperateLayout.setVisibility(View.GONE);
            } else if (position == 2) {
                mPagePoint.setVisibility(View.VISIBLE);
                mOperateLayout.setAnimation(null);
                mOperateLayout.setVisibility(View.GONE);
            } else if (position == 3) {
                mPagePoint.setVisibility(View.GONE);
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(1000L);
                mOperateLayout.setAnimation(animation);
                mOperateLayout.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    @Override
    public void onClick(View v) {
        MyApp.getPreferences().edit().putBoolean(ISFIRST, true).commit();
        startActivity(new Intent(mBaseContext, LoginActivity.class));
        overridePendingTransition(R.anim.push_left_acc, 0);
        finish();
    }
}

