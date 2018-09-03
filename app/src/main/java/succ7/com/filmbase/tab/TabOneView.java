package succ7.com.filmbase.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import succ7.com.filmbase.R;


/**
 * 仿微信tab单个view
 *
 * @author admin 2015-8-28
 */
public class TabOneView extends FrameLayout {
    private ImageView imguncheck;//图标未选中
    private ImageView imgcheck;//图标被选中
    private TextView textunchecked;//文字未选中
    private TextView textchecked;//文字被选中
    private TextView redhot;//有消息时的小红点点

    public TabOneView(Context context) {
        super(context);
        this.init(context, null);
    }

    /**
     * 在布局文件中使用的时候会调用此方法构建view对象
     */
    public TabOneView(Context context, AttributeSet set) {
        super(context, set);
        this.init(context, set);
    }

    /**
     * 初始化
     */
    private void init(Context context, AttributeSet set) {
        inflate(context, R.layout.layout_tab_one_view, this);
        this.imguncheck = (ImageView) this.findViewById(R.id.tab_img_uncheck);
        this.imgcheck = (ImageView) this.findViewById(R.id.tab_img_check);
        this.textunchecked = (TextView) this.findViewById(R.id.tab_text_unchecked);
        this.textchecked = (TextView) this.findViewById(R.id.tab_text_checked);
        this.redhot = (TextView) this.findViewById(R.id.tab_red_hot);
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.TabOneViewStylealbe);
            if (obtainStyledAttributes != null) {
                for (int indexCount = obtainStyledAttributes.getIndexCount(), i = 0; i < indexCount; ++i) {
                    final int index = obtainStyledAttributes.getIndex(i);
                    switch (index) {
                        case R.styleable.TabOneViewStylealbe_tab_src_unchecked:
                            this.imguncheck.setImageDrawable(obtainStyledAttributes.getDrawable(index));
                            break;
                        case R.styleable.TabOneViewStylealbe_tab_src_checked:
                            this.imgcheck.setImageDrawable(obtainStyledAttributes.getDrawable(index));
                            break;
                        case R.styleable.TabOneViewStylealbe_tab_text:
                            this.textunchecked.setText(obtainStyledAttributes.getString(index));
                            this.textchecked.setText(obtainStyledAttributes.getString(index));
                            break;
                        case R.styleable.TabOneViewStylealbe_tab_text_color_unchecked:
                            int color = obtainStyledAttributes.getColor(index, 0);
                            if (color != 0) {
                                this.textunchecked.setTextColor(color);
                            }
                            break;
                        case R.styleable.TabOneViewStylealbe_tab_text_color_checked:
                            int color1 = obtainStyledAttributes.getColor(index, 0);
                            if (color1 != 0) {
                                this.textchecked.setTextColor(color1);
                            }
                            break;
                    }
                }
                obtainStyledAttributes.recycle();
            }
        }
    }

    /**
     * 设置选中状态
     *
     * @param selected 选中状态
     */
    public void setSelected(boolean selected) {
        int tabViewAlpha = selected ? 255 : 0;
        this.setTabViewAlpha(tabViewAlpha);
    }

    /**
     * 设置图片及文字的透明度
     *
     * @param tabViewAlpha 透明度值
     */
    public void setTabViewAlpha(int tabViewAlpha) {
        this.setTabImageCheckViewAlpha(tabViewAlpha);
        this.setTabTextViewAlpha(tabViewAlpha);
        this.invalidate();
    }

    /**
     * 设置图标的透明度
     *
     * @param alpha 图标的透明度
     */
    private void setTabImageCheckViewAlpha(int alpha) {
        this.imgcheck.getDrawable().setAlpha(alpha);
        this.imguncheck.getDrawable().setAlpha(255 - alpha);
    }

    /**
     * 设置文字的透明度
     *
     * @param tabTextViewAlpha 文字的透明度
     */
    public void setTabTextViewAlpha(int tabTextViewAlpha) {
        this.textchecked.setTextColor(this.textchecked.getTextColors()
                .withAlpha(tabTextViewAlpha).getDefaultColor());
        this.textunchecked.setTextColor(this.textunchecked.getTextColors()
                .withAlpha(255 - tabTextViewAlpha).getDefaultColor());
    }


    /**
     * 设置提示红点的可见性及文本
     *
     * @param visibility     是否可见
     * @param unReadMsgCount 未读消息数量
     */
    public void setTabRedHotVisibility(int visibility, String unReadMsgCount) {
        this.redhot.setVisibility(visibility);
        this.redhot.setText(unReadMsgCount);
    }

    /**
     * 设置文字
     *
     * @param resId 文字资源id
     */
    public void setTabText(int resId) {
        this.textunchecked.setText(resId);
        this.textchecked.setText(resId);
    }

    /**
     * 设置文字
     *
     * @param tabText 文字
     */
    public void setTabText(String tabText) {
        this.textunchecked.setText(tabText);
        this.textchecked.setText(tabText);
    }

    /**
     * 设置未选中的图片
     *
     * @param imageResource 未选中的图片
     */
    public void setTabUnCheckedImage(int imageResource) {
        this.imguncheck.setImageResource(imageResource);
    }

    /**
     * 设置选中的图片
     *
     * @param imageResource 选中的图片
     */
    public void setTabCheckedImage(int imageResource) {
        this.imgcheck.setImageResource(imageResource);
    }
}