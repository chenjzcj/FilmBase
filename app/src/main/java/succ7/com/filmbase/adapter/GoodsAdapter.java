package succ7.com.filmbase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import succ7.com.filmbase.R;
import succ7.com.filmbase.bean.bmob.Goods;
import succ7.com.filmbase.utils.PicassoUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/25 0025 17:39
 * 兑换商品列表适配器
 */
public class GoodsAdapter extends BaseAdapter {
    private List<Goods> goodses;
    private Context context;

    public GoodsAdapter(Context context, List<Goods> goodses) {
        this.goodses = goodses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return goodses != null ? goodses.size() : 0;
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
            convertView = View.inflate(context, R.layout.item_goods, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Goods goods = goodses.get(position);
        viewHolder.tv_goodsname.setText(goods.getGoodsName());
        viewHolder.tv_goodsprice.setText(goods.getPrice());
        viewHolder.tv_goodscount.setText(goods.getCount());
        PicassoUtils.showImage(context, goods.getPic(), R.color.white, viewHolder.iv_goodspic);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_goodspic;
        TextView tv_goodsname;
        TextView tv_goodsprice;
        TextView tv_goodscount;

        public ViewHolder(View root) {
            this.iv_goodspic = (ImageView) root.findViewById(R.id.iv_goodspic);
            this.tv_goodsname = (TextView) root.findViewById(R.id.tv_goodsname);
            this.tv_goodsprice = (TextView) root.findViewById(R.id.tv_goodsprice);
            this.tv_goodscount = (TextView) root.findViewById(R.id.tv_goodscount);
        }
    }

    public void setGoodses(List<Goods> goodses) {
        this.goodses = goodses;
        this.notifyDataSetChanged();
    }
}
