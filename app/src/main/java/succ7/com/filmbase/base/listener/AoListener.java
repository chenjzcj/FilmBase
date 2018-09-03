package succ7.com.filmbase.base.listener;

import android.view.View;

import succ7.com.filmbase.R;
import succ7.com.filmbase.base.BaseUIActivity;


/**
 * Created by Administrator on 2015/5/28.
 */
public class AoListener implements View.OnClickListener {
    BaseUIActivity mBaseActivity;

    public AoListener(BaseUIActivity baseActivitys) {
        this.mBaseActivity = baseActivitys;
    }

    public void onClick(View view) {
        if (this.mBaseActivity.isPushTodown) {
            this.mBaseActivity.finish();
            this.mBaseActivity.overridePendingTransition(0, R.anim.push_down);
            return;
        }
        this.mBaseActivity.finish();
        this.mBaseActivity.overridePendingTransition(0, R.anim.push_right);
    }
}