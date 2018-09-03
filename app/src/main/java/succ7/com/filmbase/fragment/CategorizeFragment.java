package succ7.com.filmbase.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sch.rfview.decoration.DividerGridItemDecoration;
import com.sch.rfview.manager.AnimRFGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.FilmDetailActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.FindFilmsByCategoryEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ToastUtils;
import succ7.com.filmbase.view.dialog.DialogHelper;
import succ7.com.filmbase.view.dialog.WaitDialog;

/**
 * Created by shichaohui on 2015/8/4 0004.
 * 线性布局下拉刷新
 */
public class CategorizeFragment extends Fragment {

    private static final String TAG_TYPE = "type", TAG_AREA = "area", TAG_YEAR = "year", TAG_ACTOR = "actor";
    private MyAnimRFRecyclerView2 mRecyclerView;
    private View headerView;
    private View footerView;
    private List<Film> films;
    private Context mContext;
    private int mPage = 0;
    boolean isRefresh;//true为刷新,false为上拉加载
    private LinearLayout llTypeMore1;
    private List<View> typeViews = new ArrayList<>(), areaViews = new ArrayList<>(),
            yearViews = new ArrayList<>(), actorViews = new ArrayList<>();
    //电影类型
    private String type = Constants.types[0];
    private String area = Constants.areas[0];
    private String year = Constants.years[0];
    private String actor = Constants.actors[0];
    private LinearLayout llAreaMore1, llAreaMore2, llYearMore1, llYearMore2, llActorMore1, llActorMore2, llActorMore3;
    private int[] typeIds = new int[]{R.id.type0, R.id.type1, R.id.type2, R.id.type3, R.id.type4,
            R.id.type5, R.id.type6, R.id.type7, R.id.type8, R.id.type9};
    private int[] areaIds = new int[]{R.id.area0, R.id.area1, R.id.area2, R.id.area3, R.id.area4, R.id.area5, R.id.area6,
            R.id.area7, R.id.area8, R.id.area9, R.id.area10, R.id.area11, R.id.area12, R.id.area13, R.id.area14};
    private int[] yearIds = new int[]{R.id.year0, R.id.year1, R.id.year2, R.id.year3, R.id.year4, R.id.year5, R.id.year6,
            R.id.year7, R.id.year8, R.id.year9, R.id.year10};
    private int[] actorIds = new int[]{R.id.actor0, R.id.actor1, R.id.actor2, R.id.actor3, R.id.actor4, R.id.actor5, R.id.actor6,
            R.id.actor7, R.id.actor8, R.id.actor9, R.id.actor10, R.id.actor11, R.id.actor12, R.id.actor13, R.id.actor14,
            R.id.actor15, R.id.actor16, R.id.actor17, R.id.actor18, R.id.actor19};
    private WaitDialog waitDialog;

    public CategorizeFragment() {
        films = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if (mRecyclerView == null) {
            // 自定义的RecyclerView, 也可以在布局文件中正常使用
            mRecyclerView = new MyAnimRFRecyclerView2(getActivity());
            // 头部
            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_categ_header, null);
            initHeader(headerView);
            // 脚部
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);
            // 使用重写后的线性布局管理器
            mRecyclerView.setLayoutManager(new AnimRFGridLayoutManager(getActivity(), 2));
            mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity(), true));

            // 添加头部和脚部，如果不添加就使用默认的头部和脚部
            mRecyclerView.addHeaderView(headerView);
            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
            mRecyclerView.setScaleRatio(1.7f);
            // 设置下拉时拉伸的图片，不设置就使用默认的
            //mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
            mRecyclerView.addFootView(footerView);

            // 设置刷新动画的颜色
            mRecyclerView.setColor(Color.RED, Color.BLUE);
            // 设置头部恢复动画的执行时间，默认500毫秒
            mRecyclerView.setHeaderImageDurationMillis(300);
            // 设置拉伸到最高时头部的透明度，默认0.5f
            mRecyclerView.setHeaderImageMinAlpha(0.6f);
            // 设置适配器
            MyAdapter adapter = new MyAdapter();
            //参考(为RecyclerView添加item的点击事件) : http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0327/2647.html
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, Film film) {
                    FilmDetailActivity.enterFilmDetailActivity(getActivity(), film);
                }
            });
            mRecyclerView.setAdapter(adapter);

            // 设置刷新和加载更多数据的监听，分别在onRefresh()和onLoadMore()方法中执行刷新和加载更多操作
            mRecyclerView.setLoadDataListener(new MyAnimRFRecyclerView2.LoadDataListener() {
                @Override
                public void onRefresh() {
                    refreshOrLoadmore(true);
                }

                @Override
                public void onLoadMore() {
                    refreshOrLoadmore(false);
                }
            });
            // 首次刷新
            mRecyclerView.setRefresh(true);
        }
        return mRecyclerView;
    }

    private void initHeader(View headerView) {
        //类型
        headerView.findViewById(R.id.iv_type_more).setOnClickListener(new MyMoreOnClickListener());
        llTypeMore1 = (LinearLayout) headerView.findViewById(R.id.ll_type_more1);
        for (int i = 0; i < Constants.types.length; i++) {
            initType(headerView, typeIds[i], Constants.types, i, TAG_TYPE);
        }
        //地区
        headerView.findViewById(R.id.iv_area_more).setOnClickListener(new MyMoreOnClickListener());
        llAreaMore1 = (LinearLayout) headerView.findViewById(R.id.ll_area_more1);
        llAreaMore2 = (LinearLayout) headerView.findViewById(R.id.ll_area_more2);
        for (int i = 0; i < Constants.areas.length; i++) {
            initType(headerView, areaIds[i], Constants.areas, i, TAG_AREA);
        }
        //年代
        headerView.findViewById(R.id.iv_year_more).setOnClickListener(new MyMoreOnClickListener());
        llYearMore1 = (LinearLayout) headerView.findViewById(R.id.ll_year_more1);
        llYearMore2 = (LinearLayout) headerView.findViewById(R.id.ll_year_more2);
        for (int i = 0; i < Constants.years.length; i++) {
            initType(headerView, yearIds[i], Constants.years, i, TAG_YEAR);
        }
        //演员
        headerView.findViewById(R.id.iv_actor_more).setOnClickListener(new MyMoreOnClickListener());
        llActorMore1 = (LinearLayout) headerView.findViewById(R.id.ll_actor_more1);
        llActorMore2 = (LinearLayout) headerView.findViewById(R.id.ll_actor_more2);
        llActorMore3 = (LinearLayout) headerView.findViewById(R.id.ll_actor_more3);
        for (int i = 0; i < Constants.actors.length; i++) {
            initType(headerView, actorIds[i], Constants.actors, i, TAG_ACTOR);
        }
    }

    private void initType(View headerView, int resId, String[] strArray, int position, String tag) {
        TextView view = (TextView) headerView.findViewById(resId);
        view.setSelected(position == 0);//选中第一个
        view.setText(strArray[position]);
        view.setTag(strArray[position]);
        view.setOnClickListener(new MyOnClickListener(tag));
        switch (tag) {
            case TAG_TYPE:
                typeViews.add(view);
                break;
            case TAG_AREA:
                areaViews.add(view);
                break;
            case TAG_YEAR:
                yearViews.add(view);
                break;
            case TAG_ACTOR:
                actorViews.add(view);
                break;
        }
    }

    class MyMoreOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_type_more:
                    animShowOrHideView(llTypeMore1);
                    animRotate(llTypeMore1, v);
                    break;
                case R.id.iv_area_more:
                    animShowOrHideView(llAreaMore1);
                    animShowOrHideView(llAreaMore2);
                    animRotate(llAreaMore1, v);
                    break;
                case R.id.iv_year_more:
                    animShowOrHideView(llYearMore1);
                    animShowOrHideView(llYearMore2);
                    animRotate(llYearMore1, v);
                    break;
                case R.id.iv_actor_more:
                    animShowOrHideView(llActorMore1);
                    animShowOrHideView(llActorMore2);
                    animShowOrHideView(llActorMore3);
                    animRotate(llActorMore1, v);
                    break;
            }
        }
    }

    /**
     * 旋转动画
     *
     * @param view       需要判断可见性的view
     * @param rotateView 需要做旋转的view
     */
    private void animRotate(View view, View rotateView) {
        LogUtils.i("animRotate view = " + view + ",rotateView = " + rotateView);
        Animation animation = AnimationUtils.loadAnimation(mContext, view.getVisibility() == View.VISIBLE ?
                R.anim.image_rotate_anim : R.anim.image_rotate_anim_back);
        animation.setFillAfter(true);
        rotateView.startAnimation(animation);
    }

    /**
     * 动画显示或者隐藏控件
     */
    private void animShowOrHideView(View view) {
        boolean isGone = view.getVisibility() == View.GONE;
        view.startAnimation(AnimationUtils.loadAnimation(mContext, isGone ? R.anim.popup_enter : R.anim.popup_exit));
        view.setVisibility(isGone ? View.VISIBLE : View.GONE);
    }

    class MyOnClickListener implements View.OnClickListener {
        private String tag;

        public MyOnClickListener(String tag) {
            this.tag = tag;
        }

        @Override
        public void onClick(View v) {
            switch (tag) {
                case TAG_TYPE:
                    for (View view : typeViews) {
                        view.setSelected(false);
                    }
                    break;
                case TAG_AREA:
                    for (View view : areaViews) {
                        view.setSelected(false);
                    }
                    break;
                case TAG_YEAR:
                    for (View view : yearViews) {
                        view.setSelected(false);
                    }
                    break;
                case TAG_ACTOR:
                    for (View view : actorViews) {
                        view.setSelected(false);
                    }
                    break;
            }
            v.setSelected(!v.isSelected());
            clickEvent(tag, v);
        }
    }

    /**
     * 根据v处理点击事件,v里面包含文本内容
     *
     * @param tag 标签类型
     * @param v   View
     */
    private void clickEvent(String tag, View v) {
        switch (tag) {
            case TAG_TYPE:
                type = (String) v.getTag();
                break;
            case TAG_AREA:
                area = (String) v.getTag();
                break;
            case TAG_YEAR:
                year = (String) v.getTag();
                break;
            case TAG_ACTOR:
                actor = (String) v.getTag();
                break;
        }
        createWaitDialog(getString(R.string.loading)).show();
        refreshOrLoadmore(true);
    }

    private void refreshOrLoadmore(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            //下拉刷新,重新获取数据
            mPage = 0;
            BmobHelper.requestServer(mContext, Constants.REQUEST_TYPE_FIND_FILM_BY_CATEGORY,
                    null, mPage, 0, type, area, year, actor);
        } else {
            //分页每次加载10条数据
            BmobHelper.requestServer(mContext, Constants.REQUEST_TYPE_FIND_FILM_BY_CATEGORY,
                    null, ++mPage, 0, type, area, year, actor);
        }
    }

    public void refreshComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void loadMoreComplate() {
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Film film);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements View.OnClickListener {

        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_categ_film, null);
            view.setOnClickListener(this);
            view.setOnTouchListener(new View.OnTouchListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    LogUtils.i("onTouch event2 = " + MotionEvent.actionToString(event.getAction()));
                    return false;
                }
            });
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            Film film = films.get(position);
            String filmName = film.getFilmName();
            holder.tv_film_name.setText(filmName.length() > 5 ? mContext.getString(R.string.three_dot, filmName.substring(0, 4)) : filmName);
            PicassoUtils.showImage(mContext, film.getFilmPicUrl(), R.mipmap.defalut_film, holder.iv_film_pic);
            holder.tv_film_name.setOnClickListener(new FilmOperationOnclickListener(film));
            //将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(film);
        }

        @Override
        public int getItemCount() {
            return films.size();
        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }


        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, (Film) v.getTag());
            }

        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_film_pic;
        TextView tv_film_name;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.iv_film_pic = (ImageView) itemView.findViewById(R.id.iv_film_pic);
            this.tv_film_name = (TextView) itemView.findViewById(R.id.tv_film_name);
        }
    }

    class FilmOperationOnclickListener implements View.OnClickListener {
        Film film;

        public FilmOperationOnclickListener(Film film) {
            this.film = film;
        }

        @Override
        public void onClick(View v) {
            FilmDetailActivity.enterFilmDetailActivity((Activity) mContext, film);
        }
    }

    public void onEventMainThread(Object obj) {
        if (obj instanceof FindFilmsByCategoryEvent) {
            hideWaitDialog();
            FindFilmsByCategoryEvent findFilmsByCategoryEvent = (FindFilmsByCategoryEvent) obj;
            Object[] object = (Object[]) findFilmsByCategoryEvent.getObject();
            List<Film> films = (List<Film>) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                if (isRefresh) {//下拉刷新
                    this.films.clear();
                    this.films = films;
                    refreshComplate();
                    // 刷新完成后调用，必须在UI线程中
                    mRecyclerView.refreshComplate();
                } else {//上拉加载
                    List<Film> newFilms = films;
                    if (newFilms.size() == 0) {
                        //没有获取到数据
                        ToastUtils.showLong("没有数据啦");
                        mPage--;
                        mPage = (mPage < 0) ? 0 : mPage;
                    } else {
                        this.films.addAll(newFilms);
                        loadMoreComplate();
                    }
                    // 加载更多完成后调用，必须在UI线程中
                    mRecyclerView.loadMoreComplate();
                }

            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        }
    }

    public WaitDialog createWaitDialog(String msg) {
        if (waitDialog == null) {
            waitDialog = DialogHelper.getWaitDialog((Activity) mContext, msg);
        }
        waitDialog.setCancelable(true);
        return waitDialog;
    }

    public void hideWaitDialog() {
        if (waitDialog != null && waitDialog.isShowing()) {
            waitDialog.dismiss();
            waitDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
