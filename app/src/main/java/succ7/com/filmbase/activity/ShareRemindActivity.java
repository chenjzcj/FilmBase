package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;

/**
 * 分享说明
 */
public class ShareRemindActivity extends ScrollerBaseUIActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.share_remind));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_share_remind, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
    }
}
