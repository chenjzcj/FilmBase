package succ7.com.filmbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import succ7.com.filmbase.base.FragmentOper;


/**
 * @author 527633405@qq.com 2015-9-1
 */
public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    private SparseArray mSparseArray;

    public TabsFragmentPagerAdapter(FragmentManager fragmentManager, SparseArray sparseArray) {
        super(fragmentManager);
        this.mSparseArray = sparseArray;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int position, Object object) {
        super.destroyItem(viewGroup, position, object);
        ((FragmentOper) this.mSparseArray.get(position)).parseBTabFragment(null);
    }

    @Override
    public int getCount() {
        return this.mSparseArray.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ((FragmentOper) this.mSparseArray.get(position)).getFragment();
    }
}