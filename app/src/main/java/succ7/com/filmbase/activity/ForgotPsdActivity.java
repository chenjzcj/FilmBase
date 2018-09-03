package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.ResetPasswordEvent;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 忘记密码
 */
public class ForgotPsdActivity extends ScrollerBaseUIActivity {

    private EditText etBindEmail;
    private String bindEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.forgot_psd));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_forgot_psd, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        etBindEmail = (EditText) view.findViewById(R.id.et_bind_email);
        view.findViewById(R.id.btn_reset_psd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindEmail = etBindEmail.getText().toString();
                if (!MyTextUtils.isEmpty(bindEmail)) {
                    creatWaitDialog(getString(R.string.sent_resetpsd_email)).show();
                    UserHelper.resetPasswordByEmail(bindEmail);
                } else {
                    ToastUtils.showLong(getString(R.string.enjoy_remind));
                }
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof ResetPasswordEvent) {
            hideWaitDialog();
            ResetPasswordEvent resetPasswordEvent = (ResetPasswordEvent) obj;
            BmobException e = (BmobException) resetPasswordEvent.getObject();
            if (e == null) {
                ToastUtils.showLong("密码重置邮件发送成功,请到" + bindEmail + "邮箱中进行密码重置");
                onBackPressed();
            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        }
    }
}
