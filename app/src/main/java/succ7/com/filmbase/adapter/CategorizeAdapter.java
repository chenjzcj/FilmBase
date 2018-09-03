package succ7.com.filmbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Film;
import succ7.com.filmbase.utils.PicassoUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 分类电影列表适配器
 */
public class CategorizeAdapter extends BaseAdapter {
    private List<Film> films;
    private Context context;

    public CategorizeAdapter(Context context, List<Film> films) {
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
            convertView = View.inflate(context, R.layout.item_categ_film, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Film film = films.get(position);
        viewHolder.tv_film_name.setText(film.getFilmName());
        PicassoUtils.showImage(context, film.getFilmPicUrl(), R.color.white, viewHolder.iv_film_pic);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_film_pic;
        TextView tv_film_name;

        public ViewHolder(View root) {
            this.iv_film_pic = (ImageView) root.findViewById(R.id.iv_film_pic);
            this.tv_film_name = (TextView) root.findViewById(R.id.tv_film_name);
        }
    }

    public void setFilms(List<Film> films) {
        this.films = films;
        this.notifyDataSetChanged();
    }
}
