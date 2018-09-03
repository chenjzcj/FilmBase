package succ7.com.filmbase.tab;

import android.view.View;

public interface ITabClickListener {
    /**
     * tab点击事件
     *
     * @param view
     * @param tabId
     * @param viewId
     */
    void tabClick(View view, int tabId, int viewId);
}
