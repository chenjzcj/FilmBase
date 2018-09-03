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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sch.rfview.decoration.DividerItemDecoration;
import com.sch.rfview.manager.AnimRFLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.FilmDetailActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.FindFilmEvent;
import succ7.com.filmbase.eventbus.RefreshFilmEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * Created by shichaohui on 2015/8/4 0004.
 * 线性布局下拉刷新
 */
public class HotFilmFragment extends Fragment {

    private MyAnimRFRecyclerView mRecyclerView;
    private View headerView;
    private View footerView;
    private List<Film> films;
    private Context mContext;
    private int mPage = 0;
    boolean isRefresh;//true为刷新,false为上拉加载

    public HotFilmFragment() {
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
            mRecyclerView = new MyAnimRFRecyclerView(getActivity());
            // 头部
            headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, null);
            // 脚部
            footerView = LayoutInflater.from(getActivity()).inflate(R.layout.footer_view, null);
            // 使用重写后的线性布局管理器
            AnimRFLinearLayoutManager manager = new AnimRFLinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), manager.getOrientation(), true));


            // 添加头部和脚部，如果不添加就使用默认的头部和脚部
            //mRecyclerView.addHeaderView(headerView);
            // 设置头部的最大拉伸倍率，默认1.5f，必须写在setHeaderImage()之前
            //mRecyclerView.setScaleRatio(1.7f);
            // 设置下拉时拉伸的图片，不设置就使用默认的
            //mRecyclerView.setHeaderImage((ImageView) headerView.findViewById(R.id.iv_hander));
            //mRecyclerView.addFootView(footerView);

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
            mRecyclerView.setLoadDataListener(new MyAnimRFRecyclerView.LoadDataListener() {
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

    private void refreshOrLoadmore(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            //下拉刷新,重新获取数据
            mPage = 0;
            BmobHelper.requestServer(mContext, Constants.REQUEST_TYPE_FIND_ALL_FILM, null, mPage, 0);
        } else {
            //分页每次加载10条数据
            BmobHelper.requestServer(mContext, Constants.REQUEST_TYPE_FIND_ALL_FILM, null, ++mPage, 0);
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
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_film1, null);
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
            holder.tv_filmname.setText(filmName.length() > 5 ? mContext.getString(R.string.three_dot, filmName.substring(0, 4)) : filmName);
            holder.tv_filmcontributor.setText(mContext.getString(R.string.contributor, film.getUser().getUsername()));
            holder.tv_filmSource.setText(mContext.getString(R.string.filmSource, film.getFilmSource()));
            holder.tv_filmLength.setText(mContext.getString(R.string.filmLength, film.getFilmLength(), film.getUnit()));
            holder.tv_filmAddress.setText(mContext.getString(R.string.filmAddress, film.getFilmAddress()));
            PicassoUtils.showImage(mContext, film.getFilmPicUrl(), R.mipmap.defalut_film, holder.iv_filmPic);

            holder.btn_like.setText(mContext.getString(R.string.like, film.getLikes()));
            holder.btn_dislike.setText(mContext.getString(R.string.dislike, film.getDisLikes()));
            holder.btn_like.setOnClickListener(new FilmOperationOnclickListener(film));
            holder.btn_dislike.setOnClickListener(new FilmOperationOnclickListener(film));
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
        ImageView iv_filmPic;
        TextView tv_filmname;
        TextView tv_filmcontributor;
        TextView tv_filmSource;
        TextView tv_filmLength;
        TextView tv_filmAddress;
        Button btn_like;
        Button btn_dislike;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.iv_filmPic = (ImageView) itemView.findViewById(R.id.iv_filmPic);
            this.tv_filmname = (TextView) itemView.findViewById(R.id.tv_filmname);
            this.tv_filmcontributor = (TextView) itemView.findViewById(R.id.tv_filmcontributor);
            this.tv_filmSource = (TextView) itemView.findViewById(R.id.tv_filmSource);
            this.tv_filmLength = (TextView) itemView.findViewById(R.id.tv_filmLength);
            this.tv_filmAddress = (TextView) itemView.findViewById(R.id.tv_filmAddress);
            this.btn_like = (Button) itemView.findViewById(R.id.btn_like);
            this.btn_dislike = (Button) itemView.findViewById(R.id.btn_dislike);
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
        if (obj instanceof FindFilmEvent) {
            FindFilmEvent findFilmEvent = (FindFilmEvent) obj;
            Object[] object = (Object[]) findFilmEvent.getObject();
            BmobException e = (BmobException) object[1];
            if (e == null) {
                if (isRefresh) {//下拉刷新
                    films.clear();
                    films = (List<Film>) object[0];
                    refreshComplate();
                    // 刷新完成后调用，必须在UI线程中
                    mRecyclerView.refreshComplate();
                } else {//上拉加载
                    List<Film> newFilms = (List<Film>) object[0];
                    if (newFilms.size() == 0) {
                        //没有获取到数据
                        ToastUtils.showLong("没有数据啦");
                        mPage--;
                        mPage = (mPage < 0) ? 0 : mPage;
                    } else {
                        films.addAll(newFilms);
                        loadMoreComplate();
                    }
                    // 加载更多完成后调用，必须在UI线程中
                    mRecyclerView.loadMoreComplate();
                }

            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        } else if (obj instanceof RefreshFilmEvent) {
            refreshOrLoadmore(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
