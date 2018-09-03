package succ7.com.filmbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.juhe.MovieBoxOffice;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 电影票房列表适配器
 */
public class BoxOfficeAdapter extends BaseAdapter {
    private List<MovieBoxOffice> movieBoxOffices;
    private Context context;

    public BoxOfficeAdapter(Context context, List<MovieBoxOffice> movieBoxOffices) {
        this.movieBoxOffices = movieBoxOffices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieBoxOffices != null ? movieBoxOffices.size() : 0;
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
            convertView = View.inflate(context, R.layout.item_boxoffice, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MovieBoxOffice movieBoxOffice = movieBoxOffices.get(position);

        viewHolder.tv_rid.setText(movieBoxOffice.getRid() + "." + movieBoxOffice.getName());
        viewHolder.tv_wboxoffice.setText(movieBoxOffice.getWboxoffice());
        viewHolder.tv_tboxoffice.setText(movieBoxOffice.getTboxoffice());
        viewHolder.tv_wk.setText(movieBoxOffice.getWk());
        return convertView;
    }

    class ViewHolder {
        TextView tv_rid;
        TextView tv_wboxoffice;
        TextView tv_tboxoffice;
        TextView tv_wk;

        public ViewHolder(View root) {
            this.tv_rid = (TextView) root.findViewById(R.id.tv_rid);
            this.tv_wboxoffice = (TextView) root.findViewById(R.id.tv_wboxoffice);
            this.tv_tboxoffice = (TextView) root.findViewById(R.id.tv_tboxoffice);
            this.tv_wk = (TextView) root.findViewById(R.id.tv_wk);
        }
    }

    public void setMovieBoxOffices(List<MovieBoxOffice> movieBoxOffices) {
        this.movieBoxOffices = movieBoxOffices;
        this.notifyDataSetChanged();
    }
}
