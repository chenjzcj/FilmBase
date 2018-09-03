package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.UpdateEmailEvent;
import succ7.com.filmbase.eventbus.VerifyEmailEvent;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

public class BindEmailActivity extends ScrollerBaseUIActivity {

    private TextView tvEmail;
    private TextView tvEmailState;
    private LinearLayout llUpdateEmail;
    private EditText etNewemail;
    private String verifyEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.bind_email_title));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_bind_email, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        tvEmail = (TextView) view.findViewById(R.id.tv_email);
        tvEmailState = (TextView) view.findViewById(R.id.tv_email_state);
        llUpdateEmail = (LinearLayout) view.findViewById(R.id.ll_update_email);
        etNewemail = (EditText) view.findViewById(R.id.et_newemail);
        view.findViewById(R.id.btn_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = etNewemail.getText().toString();
                if (!MyTextUtils.isEmpty(newEmail)) {
                    verifyEmail = newEmail;
                    creatWaitDialog(getString(R.string.updating)).show();
                    UserHelper.updateEmail(newEmail);
                }
            }
        });
        initData();
    }

    private void initData() {
        User currentUser = UserHelper.getCurrentUser();
        tvEmail.setText(getString(R.string.bind_email, currentUser.getEmail()));
        Boolean emailVerified = currentUser.getEmailVerified();
        tvEmailState.setText(emailVerified ? getString(R.string.has_verified) : getString(R.string.not_verified));
        tvEmailState.setTextColor(getResources().getColor(emailVerified ? R.color.green : R.color.red));
        addEditListener(emailVerified);
    }

    /**
     * 添加编辑监听
     */
    private void addEditListener(final boolean emailVerified) {
        TextView textView = new TextView(mBaseActivity);
        textView.setText(emailVerified ? R.string.modify : R.string.verify);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        mTitleBar.setRightLayoutListener(textView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改或者验证
                if (!emailVerified) {
                    creatWaitDialog("正在发送验证邮箱").show();
                    verifyEmail = UserHelper.getCurrentUser().getEmail();
                    UserHelper.requestEmailVerify(UserHelper.getCurrentUser().getEmail());
                } else {
                    llUpdateEmail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof VerifyEmailEvent) {
            hideWaitDialog();
            VerifyEmailEvent verifyEmailEvent = (VerifyEmailEvent) obj;
            BmobException e = (BmobException) verifyEmailEvent.getObject();
            if (e == null) {
                ToastUtils.showShort("验证邮件发送成功,请到" + verifyEmail + "邮箱中进行激活");
                onBackPressed();
            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        } else if (obj instanceof UpdateEmailEvent) {
            hideWaitDialog();
            UpdateEmailEvent updateEmailEvent = (UpdateEmailEvent) obj;
            BmobException e = (BmobException) updateEmailEvent.getObject();
            if (e == null) {
                ToastUtils.showShort("邮箱修改成功,请到" + verifyEmail + "邮箱中进行激活");
                onBackPressed();
            } else {
                ToastUtils.showShort("操作失败" + e.getMessage());
            }
        }
    }
}
