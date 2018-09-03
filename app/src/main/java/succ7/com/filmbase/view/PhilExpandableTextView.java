package succ7.com.filmbase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/18 0018 09:26
 * Android可收缩/扩展的TextView【1】
 * http://blog.csdn.net/zhangphil/article/details/50088465
 */
public class PhilExpandableTextView extends TextView {

    // 最大的行，默认只显示3行
    private final int MAX = 1;

    // 如果完全伸展需要多少行？
    private int lines;

    private PhilExpandableTextView mPhilTextView;

    // 标记当前TextView的展开/收缩状态
    // true，已经展开
    // false，以及收缩
    private boolean expandableStatus = false;

    public PhilExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPhilTextView = this;

        init();
    }

    private void init() {

        // ViewTreeObserver View观察者，在View即将绘制但还未绘制的时候执行的，在onDraw之前
        final ViewTreeObserver mViewTreeObserver = this.getViewTreeObserver();

        mViewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                // 避免重复监听
                mPhilTextView.getViewTreeObserver().removeOnPreDrawListener(this);

                lines = getLineCount();
                // Log.d(this.getClass().getName(), lines+"");

                return true;
            }
        });

        setMaxLines(MAX);

        //setEllipsize(TextUtils.TruncateAt.END);
    }

    // 是否展开或者收缩，
    // true，展开；
    // false，不展开
    public void setExpandable(boolean isExpand) {
        if (isExpand) {
            setMaxLines(lines + 1);
        } else
            setMaxLines(MAX);

        expandableStatus = isExpand;
    }

    public boolean getExpandableStatus() {
        return expandableStatus;
    }
}

