package succ7.com.filmbase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2016/5/27 0027
 */
public class MyViewPager extends android.support.v4.view.ViewPager {

    private boolean canScroll = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (canScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }

    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}
