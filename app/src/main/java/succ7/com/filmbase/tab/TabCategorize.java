package succ7.com.filmbase.tab;


import succ7.com.filmbase.base.FragmentOper;
import succ7.com.filmbase.base.TabBaseFragment;

/**
 * 历史记录
 *
 * @author admin 2015-9-1
 */
public class TabCategorize extends FragmentOper {
    @Override
    public TabBaseFragment getBTFragment() {
        return new TabCategorizeFragment();
    }
}
