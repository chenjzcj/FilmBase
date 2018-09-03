package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.UpdatePsdEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 重置密码页面
 */
public class UpdatePsdActivity extends ScrollerBaseUIActivity {

    private EditText etOldPsd;
    private EditText etNewPsd;
    private EditText etNewPsdConfirm;
    private Button btnResetPsd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.reset_psd));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_update_psd, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        etOldPsd = (EditText) view.findViewById(R.id.et_old_psd);
        etNewPsd = (EditText) view.findViewById(R.id.et_new_psd);
        etNewPsdConfirm = (EditText) view.findViewById(R.id.et_new_psd_confirm);
        btnResetPsd = (Button) view.findViewById(R.id.btn_reset_psd);
        btnResetPsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPad = etOldPsd.getText().toString();
                String newPsd = etNewPsd.getText().toString();
                String newPsd2 = etNewPsdConfirm.getText().toString();
                if (MyTextUtils.isEmpty(oldPad) || MyTextUtils.isEmpty(newPsd) || MyTextUtils.isEmpty(newPsd2)) {
                    ToastUtils.showShortToast(mBaseActivity, "还有没有填写完哦");
                } else {
                    if (!newPsd.equals(newPsd2)) {
                        ToastUtils.showShortToast(mBaseActivity, "两次密码不一致");
                    } else {
                        creatWaitDialog(getString(R.string.reseting_psd)).show();
                        UserHelper.updatePassword(oldPad, newPsd);
                    }
                }
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof UpdatePsdEvent) {
            hideWaitDialog();
            UpdatePsdEvent updatePsdEvent = (UpdatePsdEvent) obj;
            Object[] object = (Object[]) updatePsdEvent.getObject();
            BmobException e = (BmobException) object[0];
            if (e == null) {
                ToastUtils.showLong("修改成功");
                onBackPressed();
            } else {
                int errorCode = e.getErrorCode();
                if (errorCode == 210) {
                    ToastUtils.showLong("原密码输入错误");
                    LogUtils.i("修改失败: (" + e.getErrorCode() + ")" + e.getMessage());
                }

            }
        }
    }
}
