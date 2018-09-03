package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.UpdateInfo;
import succ7.com.filmbase.eventbus.FindUpdateInfoEvent;
import succ7.com.filmbase.utils.ApkUtils;
import succ7.com.filmbase.utils.AppUtils;
import succ7.com.filmbase.utils.ToastUtils;
import succ7.com.filmbase.utils.UpdateUtils;

/**
 * 关于本APP
 */
public class AboutActivity extends ScrollerBaseUIActivity implements View.OnClickListener {
    TextView version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.about));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_about, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        version = (TextView) view.findViewById(R.id.tv_version);
        view.findViewById(R.id.rl_update).setOnClickListener(this);
        view.findViewById(R.id.rl_feedback).setOnClickListener(this);
        version.setText(getString(R.string.version_info, AppUtils.getVersionName(mBaseActivity), AppUtils.getVersionCode(mBaseActivity)));
        version.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_version:
                ApkUtils.openAppInMarket(mBaseActivity);//给彩虹桥好评
                break;
            case R.id.rl_update:
                creatWaitDialog(getString(R.string.getting_last_version_info)).show();
                UpdateUtils.checkUpdate(mBaseActivity);
                break;
            case R.id.rl_feedback:
                enterActivity(mBaseActivity, FeedbackActivity.class, false);
                break;
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof FindUpdateInfoEvent) {
            hideWaitDialog();
            FindUpdateInfoEvent findUpdateInfoEvent = (FindUpdateInfoEvent) obj;
            Object[] object = (Object[]) findUpdateInfoEvent.getObject();
            Object o = object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                List<UpdateInfo> updateInfos = (List<UpdateInfo>) o;
                if (updateInfos.size() < 1) {
                    ToastUtils.showLong("版本信息获取失败");
                } else {
                    UpdateInfo updateInfo = updateInfos.get(0);
                    String versionCode = updateInfo.getVersionCode();
                    if (Integer.parseInt(versionCode) > AppUtils.getVersionCode(mBaseActivity)) {
                        //有新版本,需要更新
                        UpdateUtils.showUpdateDialog(updateInfo.getApkDownloadUrl(),
                                mBaseActivity, updateInfo.getContent(), updateInfo.isForce());
                    } else {
                        ToastUtils.showShortToast(mBaseActivity, getString(R.string.cur_is_least_version));
                    }
                }
            } else {
                ToastUtils.showLong("获取资源失败: " + e.getMessage());
            }
        }
    }
}
