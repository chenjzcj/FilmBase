package succ7.com.filmbase.tab;

import android.view.View;

/**
 * 单个taboneview的点击事件监听
 */
public class TabOneClickListener implements View.OnClickListener {
    TabViewGroup mViewGroup;
    private int tabId;

    public TabOneClickListener(TabViewGroup viewGroup, int tabId) {
        this.mViewGroup = viewGroup;
        this.tabId = tabId;
    }

    @Override
    public void onClick(View view) {
        this.mViewGroup.setTabOneViewSelected(this.tabId);
        if (this.mViewGroup.IListener != null) {
            this.mViewGroup.IListener.tabClick(view, this.tabId, view.getId());
        }
    }
}
