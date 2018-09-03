package succ7.com.filmbase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import succ7.com.filmbase.R;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/15 0015
 */
public class PagePointlView extends LinearLayout {
    private Context mContext;
    private int mCount;
    private int mCur;
    private int density;//密度

    public PagePointlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PagePointlView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        density = (int) (4.0f * context.getResources().getDisplayMetrics().density);
    }

    public int getCount() {
        return this.mCount;
    }

    public int getIndex() {
        return this.mCur;
    }

    public void setCount(int count) {
        mCount = count;
        removeAllViews();
        for (int i = 0; i < mCount; ++i) {
            final ImageView imageView = new ImageView(mContext);
            imageView.setPadding(density, 0, density, 0);
            imageView.setId(i);
            if (i == mCur) {
                imageView.setImageResource(R.mipmap.page_point_on);
            } else {
                imageView.setImageResource(R.mipmap.page_point_off);
            }
            this.addView((View) imageView);
        }
    }

    public void setIndex(int index) {
        mCur = index;
        for (int i = 0; i < mCount; ++i) {
            final ImageView imageView = (ImageView) this.findViewById(i);
            if (i == mCur) {
                imageView.setImageResource(R.mipmap.page_point_on);
            } else {
                imageView.setImageResource(R.mipmap.page_point_off);
            }
        }
    }

}
