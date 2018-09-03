package succ7.com.filmbase.base;

import android.support.v4.app.Fragment;

/**
 * Fragment操作类
 *
 * @author admin 2015-8-27
 */
public abstract class FragmentOper {
    protected TabBaseFragment mBTabFragment;
    protected int b;
    private boolean isShowTitleBar;

    public abstract TabBaseFragment getBTFragment();

    public FragmentOper() {
        this.mBTabFragment = null;
        this.b = 0;
        this.isShowTitleBar = true;
    }

    public void setH(int b) {
        this.b = b;
        if (this.mBTabFragment != null) {
            this.mBTabFragment.setH(this.b);
        }
    }

    public void parseBTabFragment(Fragment fragment) {
        if (this.mBTabFragment == null) {
            this.mBTabFragment = (TabBaseFragment) fragment;
        }
    }

    void setBaseTabFragment(TabBaseFragment tabBase) {
        this.mBTabFragment = tabBase;
    }

    public void showTitleBar(boolean bool) {
        this.isShowTitleBar = bool;
        if (this.mBTabFragment == null || this.mBTabFragment.isRemoving()) {
            return;
        }
        this.mBTabFragment.showtitleBar(bool);
    }

    public Fragment getFragment() {
        if (this.mBTabFragment == null) {
            this.mBTabFragment = this.getBTFragment();
        }
        this.mBTabFragment.setH(this.b);
        this.mBTabFragment.showtitleBar(this.isShowTitleBar);
        return this.mBTabFragment;
    }

    public void d() {
        if (this.mBTabFragment == null | this.mBTabFragment.isRemoving()) {
            return;
        }
        if (this.mBTabFragment.hasView()) {
            this.mBTabFragment.loadFragmentView();
            return;
        }
        this.mBTabFragment.j();
    }
}