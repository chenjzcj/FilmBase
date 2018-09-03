package succ7.com.filmbase.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.Goods;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.LoginRequestEvent;
import succ7.com.filmbase.utils.NumUtil;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 商品兑换详情页
 */
public class GoodsExchangeDetailActivity extends ScrollerBaseUIActivity {

    private Goods goods;

    /**
     * 打开商品兑换详情页面
     *
     * @param activity 上下文
     * @param goods    商品
     */
    public static void enterGoodsExchangeDetailActivity(Activity activity, Goods goods) {
        Intent intent = new Intent(activity, GoodsExchangeDetailActivity.class);
        intent.putExtra("goods", goods);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.exchange_detail));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_goods_exchange_detail, null);
        this.addMainView(view);
        goods = (Goods) getIntent().getSerializableExtra("goods");
        initView(view);
        addEditListener();
    }

    private void initView(View view) {
        ImageView ivGoodspic = (ImageView) view.findViewById(R.id.iv_goodspic);
        TextView tvGoodsName = (TextView) view.findViewById(R.id.tv_goods_name);
        TextView tvActTime = (TextView) view.findViewById(R.id.tv_act_time);
        TextView tvActEndtime = (TextView) view.findViewById(R.id.tv_act_endtime);
        TextView tvNeedCoin = (TextView) view.findViewById(R.id.tv_need_coin);
        Button btnExchange = (Button) view.findViewById(R.id.btn_exchange);

        PicassoUtils.showImage(mBaseActivity, goods.getPic(), ivGoodspic);
        tvGoodsName.setText(goods.getGoodsName());
        tvActTime.setText(goods.getActTime());
        tvActEndtime.setText(goods.getEndTime());
        tvNeedCoin.setText(goods.getPrice());
        btnExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(GoodsExchangeDetailActivity.this.getClass().getSimpleName()));
                    return;
                }
                long filmCoinCount = UserHelper.getCurrentUser().getFilmCoinCount();
                if (filmCoinCount < NumUtil.getNumFromStr(goods.getPrice())) {
                    ToastUtils.showLong("你的影币数量为" + filmCoinCount + "个,不能兑换此商品");
                } else {
                    exchange();
                }
            }
        });
    }

    /**
     * 添加编辑监听
     */
    private void addEditListener() {
        TextView textView = new TextView(mBaseActivity);
        textView.setText(R.string.exchange_record);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        mTitleBar.setRightLayoutListener(textView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong("你还没有任何兑换记录");
            }
        });
    }

    private void exchange() {
        //兑换
    }
}
