package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.Constants;
import succ7.com.filmbase.R;
import succ7.com.filmbase.adapter.GoodsAdapter;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Goods;
import succ7.com.filmbase.bmob.BmobHelper;
import succ7.com.filmbase.eventbus.FindGoodsEvent;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 影币兑换说明
 */
public class MallActivity extends ScrollerBaseUIActivity {

    private GridView gvGoods;
    private List<Goods> goodses;
    private GoodsAdapter goodsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.exchange_mall));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_mall, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        gvGoods = (GridView) view.findViewById(R.id.gv_goods);
        goodses = new ArrayList<>();
        BmobHelper.requestServer(mBaseActivity, Constants.REQUEST_TYPE_FIND_GOODS, creatWaitDialog(getString(R.string.loading)));
        goodsAdapter = new GoodsAdapter(mBaseActivity, goodses);
        gvGoods.setAdapter(goodsAdapter);
        gvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsExchangeDetailActivity.enterGoodsExchangeDetailActivity(MallActivity.this, goodses.get(position));
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof FindGoodsEvent) {
            hideWaitDialog();
            FindGoodsEvent findGoodsEvent = (FindGoodsEvent) obj;
            Object[] object = (Object[]) findGoodsEvent.getObject();
            BmobException e = (BmobException) object[1];
            if (e == null) {
                goodses = (List<Goods>) object[0];
                goodsAdapter.setGoodses(goodses);
                gvGoods.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goodsAdapter.notifyDataSetChanged();
                    }
                }, 10);
            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        }
    }
}
