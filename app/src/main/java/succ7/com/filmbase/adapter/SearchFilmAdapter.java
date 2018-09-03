package succ7.com.filmbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Film;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 搜索电影列表适配器,只显示电影名称
 */
public class SearchFilmAdapter extends BaseAdapter {
    private List<Film> films;
    private Context context;

    public SearchFilmAdapter(Context context, List<Film> films) {
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
            convertView = View.inflate(context, R.layout.item_search_film, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Film film = films.get(position);
        viewHolder.tv_filmname.setText(film.getFilmName());
        return convertView;
    }

    class ViewHolder {
        TextView tv_filmname;

        public ViewHolder(View root) {
            this.tv_filmname = (TextView) root.findViewById(android.R.id.text1);
        }
    }

    public void setFilms(List<Film> films) {
        this.films = films;
        this.notifyDataSetChanged();
    }
}
