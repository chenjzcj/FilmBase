package succ7.com.filmbase.tab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 仿微信tab组
 *
 * @author admin 2015-8-28
 */
public class TabViewGroup extends LinearLayout {
    public ITabClickListener IListener;

    public TabViewGroup(Context context, AttributeSet set) {
        super(context, set);
        this.IListener = null;
    }

    private float getAlpha(float alpha) {
        return 255.0f * alpha;
    }

    public TabOneView getTab(int n) {
        View child = this.getChildAt(n);
        if (child != null && child instanceof TabOneView) {
            return (TabOneView) child;
        }
        return null;
    }

    public void setTabViewAlpha(int n, int n2, float n3) {
        if (n3 != 0.0f) {
            TabOneView a = this.getTab(n);
            TabOneView a2 = this.getTab(n2);
            if (a2 != null && a != null) {
                int tabViewAlpha = (int) Math.ceil(this.getAlpha(n3));
                a.setTabViewAlpha(255 - tabViewAlpha);
                a2.setTabViewAlpha(tabViewAlpha);
            }
        }
    }

    public int getTabCount() {
        return this.getChildCount();
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int tabCount = this.getTabCount(), i = 0; i < tabCount; ++i) {
            TabOneView a = this.getTab(i);
            if (a != null) {
                a.setOnClickListener(new TabOneClickListener(this, i));
                a.setSelected(false);
            }
        }
    }

    public void setTabClickListener(ITabClickListener listener) {
        this.IListener = listener;
    }

    public void setTabOneViewSelected(int n) {
        int tabCount = this.getTabCount();
        if (n < tabCount) {
            for (int i = 0; i < tabCount; ++i) {
                TabOneView a = this.getTab(i);
                if (a != null) {
                    a.setSelected(n == i);
                }
            }
        }
    }

}