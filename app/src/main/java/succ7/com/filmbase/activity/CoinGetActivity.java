package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;

/**
 * 影币获取说明
 */
public class CoinGetActivity extends ScrollerBaseUIActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.coin_info));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_coin_get, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {

    }
}
