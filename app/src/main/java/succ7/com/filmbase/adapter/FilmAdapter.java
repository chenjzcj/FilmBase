package succ7.com.filmbase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;
import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.FilmDetailActivity;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.utils.PicassoUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 电影列表适配器
 */
public class FilmAdapter extends BaseAdapter {
    private List<Film> films;
    private Context context;
    private String filmName;

    public FilmAdapter(Context context, List<Film> films) {
        this.films = films;
        this.context = context;
    }

    @Override
    public int getCount() {
        return films != null ? films.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_film, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Film film = films.get(position);
        filmName = film.getFilmName();
        viewHolder.tv_filmname.setText(filmName.length() > 5 ? context.getString(R.string.three_dot, filmName.substring(0, 4)) : filmName);
        viewHolder.tv_filmcontributor.setText(context.getString(R.string.contributor, film.getUser().getUsername()));
        viewHolder.tv_filmSource.setText(context.getString(R.string.filmSource, film.getFilmSource()));
        viewHolder.tv_filmLength.setText(context.getString(R.string.filmLength, film.getFilmLength(), film.getUnit()));
        viewHolder.tv_filmAddress.setText(context.getString(R.string.filmAddress, film.getFilmAddress()));

        PicassoUtils.showImage(context, film.getFilmPicUrl(), R.mipmap.defalut_film, viewHolder.iv_filmPic);

        viewHolder.btn_like.setText(context.getString(R.string.like, film.getLikes()));
        viewHolder.btn_dislike.setText(context.getString(R.string.dislike, film.getDisLikes()));
        viewHolder.btn_like.setOnClickListener(new FilmOperationOnclickListener(film));
        viewHolder.btn_dislike.setOnClickListener(new FilmOperationOnclickListener(film));
        return convertView;
    }

    class ViewHolder {
        ImageView iv_filmPic;
        TextView tv_filmname;
        TextView tv_filmcontributor;
        TextView tv_filmSource;
        TextView tv_filmLength;
        TextView tv_filmAddress;
        Button btn_like;
        Button btn_dislike;

        public ViewHolder(View root) {
            this.iv_filmPic = (ImageView) root.findViewById(R.id.iv_filmPic);
            this.tv_filmname = (TextView) root.findViewById(R.id.tv_filmname);
            this.tv_filmcontributor = (TextView) root.findViewById(R.id.tv_filmcontributor);
            this.tv_filmSource = (TextView) root.findViewById(R.id.tv_filmSource);
            this.tv_filmLength = (TextView) root.findViewById(R.id.tv_filmLength);
            this.tv_filmAddress = (TextView) root.findViewById(R.id.tv_filmAddress);
            this.btn_like = (Button) root.findViewById(R.id.btn_like);
            this.btn_dislike = (Button) root.findViewById(R.id.btn_dislike);
        }
    }

    public void setFilms(List<Film> films) {
        this.films = films;
        this.notifyDataSetChanged();
    }

    class FilmOperationOnclickListener implements View.OnClickListener {
        Film film;

        public FilmOperationOnclickListener(Film film) {
            this.film = film;
        }

        @Override
        public void onClick(View v) {
            FilmDetailActivity.enterFilmDetailActivity((Activity) context, film);
        }
    }
}
