package succ7.com.filmbase.activity;

import android.os.Bundle;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.AppManager;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.FragmentOper;
import succ7.com.filmbase.base.HomeBaseUiActivity;
import succ7.com.filmbase.bean.bmob.UpdateInfo;
import succ7.com.filmbase.eventbus.FindUpdateInfoEvent;
import succ7.com.filmbase.eventbus.LoginRequestEvent;
import succ7.com.filmbase.utils.AppUtils;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.ToastUtils;
import succ7.com.filmbase.utils.UpdateUtils;

/**
 * 作者 : 527633405@qq.com
 * 时间 : 2015/12/15 0015
 */
public class HomeActivity extends HomeBaseUiActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        for (int i = 0; i < this.mSpareArray.size(); ++i) {
            ((FragmentOper) this.mSpareArray.get(i)).showTitleBar(true);
        }
        UpdateUtils.checkUpdate(mBaseActivity);
    }

    /**
     * 再按一次退出程序
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
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
                if (updateInfos.size() >= 1) {
                    UpdateInfo updateInfo = updateInfos.get(0);
                    String versionCode = updateInfo.getVersionCode();
                    if (Integer.parseInt(versionCode) > AppUtils.getVersionCode(mBaseActivity)) {
                        //有新版本,需要更新
                        UpdateUtils.showUpdateDialog(updateInfo.getApkDownloadUrl(),
                                mBaseActivity, updateInfo.getContent(), updateInfo.isForce());
                    }
                }
            }
        } else if (obj instanceof LoginRequestEvent) {
            LoginRequestEvent loginRequestEvent = (LoginRequestEvent) obj;
            String loginRequestEventObject = (String) loginRequestEvent.getObject();
            LogUtils.i("loginRequestEventObject = " + loginRequestEventObject);
            switch (loginRequestEventObject) {
                case "GoodsExchangeDetailActivity":
                case "FilmDetailActivity":
                    AppManager.getAppManager().finishAllActivity();
                    break;
            }
            enterLoginActivity();
        }
    }

    /**
     * 此操作需要先登录
     */
    private void enterLoginActivity() {
        ToastUtils.showLong(getString(R.string.pleaseloginfirst));
        enterActivity(this, LoginActivity.class, true);
    }
}
