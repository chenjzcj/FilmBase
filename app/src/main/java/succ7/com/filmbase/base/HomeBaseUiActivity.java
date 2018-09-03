package succ7.com.filmbase.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import de.greenrobot.event.EventBus;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.TabsFragmentPagerAdapter;
import succ7.com.filmbase.tab.ITabClickListener;
import succ7.com.filmbase.tab.TabHot;
import succ7.com.filmbase.tab.TabMe;
import succ7.com.filmbase.tab.TabCategorize;
import succ7.com.filmbase.tab.TabOneView;
import succ7.com.filmbase.tab.TabViewGroup;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.view.MyViewPager;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/16 0016
 * 主页ui主要操控对象
 */
public class HomeBaseUiActivity extends BaseUIActivity implements ITabClickListener {
    private static int unReadMsgCount = 0;
    public MyViewPager mViewpager;
    public TabViewGroup mTabGroup;
    public TabOneView mTabHome, mTabMsg, mTabSetting;
    protected SparseArray mSpareArray;
    protected TabsFragmentPagerAdapter tabPagerAdapter;

    /**
     * 构造方法
     */
    public HomeBaseUiActivity() {
        this.mSpareArray = new SparseArray();
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.initSpareArray();
        int cur_item;// 当前tab项
        if (bundle != null) {
            cur_item = bundle.getInt("cur_item", 0);
        } else {
            cur_item = 0;
        }
        this.mViewpager = (MyViewPager) this.findViewById(R.id.view_pager);
        this.mViewpager.addOnPageChangeListener(new MPageChangeListener(this));

        this.mTabGroup = (TabViewGroup) this.findViewById(R.id.tab_group);
        mTabGroup.setTabClickListener(this);

        this.mTabHome = (TabOneView) this.findViewById(R.id.tab_home_tabview);
        this.mTabMsg = (TabOneView) this.findViewById(R.id.tab_msg_tabview);
        this.mTabSetting = (TabOneView) this.findViewById(R.id.tab_setting_tabview);
        this.ToBaseTabFrgment();
        this.initViewPage();
        this.setTabSelecteds(cur_item);
        //注册eventbus
        EventBus.getDefault().register(this);
    }

    /**
     * 初始化数组
     */
    public void initSpareArray() {
        this.mSpareArray.put(0, new TabHot());
        this.mSpareArray.put(1, new TabCategorize());
        this.mSpareArray.put(2, new TabMe());
    }

    private void ToBaseTabFrgment() {
        for (int i = 0; i < this.mSpareArray.size(); ++i) {
            //将mSpareArray中的FragmentOper遍历出来,并执行parseBTabFragment操作
            FragmentOper fragmentOper = (FragmentOper) this.mSpareArray.get(i);
            fragmentOper.parseBTabFragment(this.getFragment(i));
        }
    }

    /**
     * @param index 角标
     * @return
     */
    public Fragment getFragment(int index) {
        return this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(this.mViewpager.getId(), index));
    }

    /**
     * 通过角标与viewpagerId返回一个tag
     *
     * @param viewpagerId
     * @param index
     * @return
     */
    private String getFragmentTag(int viewpagerId, long index) {
        return "android:switcher:" + viewpagerId + ":" + index;
    }

    public void setViewpagerCanScroll(boolean canScroll) {
        if (mViewpager != null) {
            mViewpager.setCanScroll(canScroll);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.home_menu_layout;
    }

    /**
     * ViewPage初始化
     */
    protected void initViewPage() {
        this.tabPagerAdapter = new TabsFragmentPagerAdapter(this.getSupportFragmentManager(), this.mSpareArray);
        this.mViewpager.setAdapter(this.tabPagerAdapter);
    }

    public void setOffscreenPageLimit(int n) {
        if (n != 0) {
            int offscreenPageLimit;
            if (n == 1) {
                offscreenPageLimit = 3;
            } else {
                offscreenPageLimit = 4;
            }
            if (this.mViewpager.getOffscreenPageLimit() < offscreenPageLimit) {
                this.mViewpager.setOffscreenPageLimit(offscreenPageLimit);
            }
        }
    }

    public FragmentOper getFragmentOper(int n) {
        return (FragmentOper) this.mSpareArray.get(n);
    }

    public void setTabSelecteds(int tabOneViewSelected) {
        this.mTabGroup.setTabOneViewSelected(tabOneViewSelected);
    }

    public void a(int tabOneViewSelected, boolean b) {
        this.mTabGroup.setTabOneViewSelected(tabOneViewSelected);
        this.mViewpager.setCurrentItem(tabOneViewSelected, b);
    }

    public int getCurrentItem() {
        return this.mViewpager.getCurrentItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mSpareArray.clear();
        // 注销eventbus
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        LogUtils.i("HomeBaseUiActivity -  >  onStart");
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            bundle.putInt("cur_item", this.mViewpager.getCurrentItem());
        }
    }

    /**
     * tab点击事件
     *
     * @param view   子view
     * @param tabId  tabId
     * @param viewId viewId
     */
    @Override
    public void tabClick(View view, int tabId, int viewId) {
        LogUtils.i("tabId = " + tabId);
        this.mViewpager.setCurrentItem(tabId, false);
    }

    private void setTabRedHotVisibility(int visibility, String unReadMsgCount) {
        if (this.mTabMsg != null) {
            this.mTabMsg.setTabRedHotVisibility(visibility, unReadMsgCount);
        }
    }

    public void onEventMainThread(Object obj) {

    }

    public class MPageChangeListener implements Handler.Callback, ViewPager.OnPageChangeListener {
        HomeBaseUiActivity mHBActivity;
        private int tempSelectPos;
        private int curSelectPos;
        private int state;
        private Handler mHandler;

        public MPageChangeListener(HomeBaseUiActivity activity) {
            this.mHBActivity = activity;
            this.tempSelectPos = 0;
            this.curSelectPos = 0;
            // state==1的时候表示正在滑动，state==2的时候表示滑动完毕了，state==0的时候表示什么都没做。
            this.state = 0;
            this.mHandler = new Handler(this);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    if (this.curSelectPos != 0)
                        this.mHBActivity.getFragmentOper(this.curSelectPos).d();
                    break;
                }
            }
            return false;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            switch (this.state) {
                case 1:
                case 2: {
                    int curPosition;
                    if (this.mHBActivity.mViewpager.getCurrentItem() >= position) {
                        curPosition = position;
                        position = position + 1;
                    } else {
                        curPosition = position + 1;
                        positionOffset = 1.0f - positionOffset;
                    }
                    this.mHBActivity.mTabGroup.setTabViewAlpha(curPosition, position, positionOffset);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            this.tempSelectPos = this.curSelectPos;
            this.curSelectPos = position;
            this.mHBActivity.setOffscreenPageLimit(position);
            if (this.state == 0) {
                this.mHandler.removeMessages(1);
                this.mHandler.sendEmptyMessageDelayed(1, 100);
            }
            LogUtils.i("position = " + position);
            if (position == 1) {
                //消除新消息标志
                unReadMsgCount = 0;
                setTabRedHotVisibility(View.GONE, unReadMsgCount + "");
            }
            hideSoftInputMethod();//只要有页面切换,都要进行软键盘的隐藏
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            this.mHandler.removeMessages(1);
            if (state != ViewPager.SCROLL_STATE_IDLE) {
                this.state = state;
            } else {
                this.state = state;
                if (this.curSelectPos != this.tempSelectPos) {
                    this.mHBActivity.setTabSelecteds(this.curSelectPos);
                    this.mHandler.sendEmptyMessageDelayed(1, 100);
                }
            }

        }
    }
}
