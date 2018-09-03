package succ7.com.filmbase.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.AddFilmActivity;
import succ7.com.filmbase.activity.SearchActivity;
import succ7.com.filmbase.base.TabBaseFragment;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.LoginRequestEvent;
import succ7.com.filmbase.fragment.HotFilmFragment;
import succ7.com.filmbase.utils.LogUtils;


/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/16 0016
 * 热门电影
 */
public class TabHotFragment extends TabBaseFragment {

    private TextView tvSearch;

    @Override
    public void fragmentView() {
        LogUtils.i("TabHotFragment ->fragmentView");
    }

    @Override
    public void errorViewListener() {
        LogUtils.i("TabHotFragment ->errorViewListener");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.i("TabHotFragment ->onAttach");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.i("TabHotFragment ->onViewCreated");
        this.initView(this.getView());
    }

    /**
     * 设置标题栏右边的View
     */
    private void setRightView() {
        ImageView addFilm = new ImageView(mContext);
        addFilm.setImageResource(R.mipmap.ic_add);
        addFilm.setScaleType(ImageView.ScaleType.CENTER);
        this.titlebar.setRightLayoutListener(addFilm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(TabHotFragment.this.getClass().getSimpleName()));
                    return;
                }
                AddFilmActivity.enterAddFilmActivity(getActivity(), "", null);
            }
        });
        this.titlebar.setRightViewVisibility(View.VISIBLE);
    }

    private void initView(View view) {
        this.titlebar.setTitle(mContext.getString(R.string.hot));
        this.mViewStub.setLayoutResource(R.layout.tab_hot1);
        this.mViewStub.inflate();
        this.setRightView();
        tvSearch = (TextView) view.findViewById(R.id.tv_search);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入搜索页面
                enterActivity(SearchActivity.class);
            }
        });
        this.hideProgress();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new HotFilmFragment()).commit();
    }

    public void enterActivity(Class cls) {
        startActivity(new Intent(mContext, cls));
        getActivity().overridePendingTransition(R.anim.push_left_acc, 0);
    }
}
