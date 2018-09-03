package succ7.com.filmbase.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.BoxOfficeActivtity;
import succ7.com.filmbase.base.TabBaseFragment;
import succ7.com.filmbase.fragment.CategorizeFragment;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/16 0016
 * 电影分类
 */
public class TabCategorizeFragment extends TabBaseFragment {
    @Override
    public void fragmentView() {
        this.initView(this.getView());
    }

    @Override
    public void errorViewListener() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView(View view) {
        titlebar.setTitle(mContext.getString(R.string.categorize));
        this.mViewStub.setLayoutResource(R.layout.tab_categorize);
        this.mViewStub.inflate();
        this.setRightView();
        this.hideProgress();
        getFragmentManager().beginTransaction().replace(R.id.categ_fragment_container, new CategorizeFragment()).commit();

    }

    /**
     * 设置标题栏右边的View
     */
    private void setRightView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setText(R.string.box_office);
        this.titlebar.setRightLayoutListener(textView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoxOfficeActivtity.enterBoxOfficeActivtity((Activity) mContext);
            }
        });
        this.titlebar.setRightViewVisibility(View.VISIBLE);
    }

}
