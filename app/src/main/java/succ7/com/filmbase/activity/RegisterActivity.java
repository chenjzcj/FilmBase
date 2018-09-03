package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.AppManager;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.CheckInviteCodeEvent;
import succ7.com.filmbase.eventbus.LoginEvent;
import succ7.com.filmbase.eventbus.RegisterEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 用户注册页面
 */
public class RegisterActivity extends ScrollerBaseUIActivity {

    private EditText etPsd, etUsername, etEmail, etInvitePeopleCode;
    private Button btnRegister;
    private String userName, psd, email, invitePeopleCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.user_register));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_register, null);
        this.addMainView(view);
        initView(view);
    }

    protected void initView(View view) {
        etUsername = (EditText) view.findViewById(R.id.et_username);
        etPsd = (EditText) view.findViewById(R.id.et_password);
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etInvitePeopleCode = (EditText) view.findViewById(R.id.et_invitePeopleCode);
        view.findViewById(R.id.tv_noreg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterActivity(mBaseActivity, HomeActivity.class, true);
            }
        });
        btnRegister = (Button) view.findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUsername.getText().toString();
                psd = etPsd.getText().toString();
                email = etEmail.getText().toString();
                invitePeopleCode = etInvitePeopleCode.getText().toString();
                if (MyTextUtils.isEmpty(userName) || MyTextUtils.isEmpty(psd) || MyTextUtils.isEmpty(email)) {
                    ToastUtils.showShortToast(RegisterActivity.this, getString(R.string.enjoy_remind));
                } else {
                    if (MyTextUtils.isEmpty(invitePeopleCode)) {
                        //如果邀请码为空,说明用户不想填写,那就直接让他注册得了,不检查邀请码的合法性了
                        creatWaitDialog(getString(R.string.registerring)).show();
                        UserHelper.signUp(userName, psd, email, "000000");
                    } else {
                        if (invitePeopleCode.length() != 6) {
                            ToastUtils.showLong(getString(R.string.error_invitecode));
                            return;
                        }
                        creatWaitDialog(getString(R.string.registerring)).show();
                        UserHelper.checkInviteCodeExist(invitePeopleCode);
                    }
                }
            }
        });
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof RegisterEvent) {
            hideWaitDialog();
            RegisterEvent registerEvent = (RegisterEvent) obj;
            Object[] object = (Object[]) registerEvent.getObject();
            Object o = object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                creatWaitDialog(getString(R.string.register_succ_please_login)).show();
                UserHelper.login(userName, psd);
                AppManager.getAppManager().finishActivity(LoginActivity.class);
            } else {
                if (e.getErrorCode() == 202) {
                    //ToastUtils.showLong("注册失败:( " + e.getErrorCode() + ")" + e.getMessage());
                    ToastUtils.showLong("该用户名已被注册");
                }
            }
        } else if (obj instanceof CheckInviteCodeEvent) {
            CheckInviteCodeEvent checkInviteCodeEvent = (CheckInviteCodeEvent) obj;
            Object[] object = (Object[]) checkInviteCodeEvent.getObject();
            List<User> users = (List<User>) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                if (users.size() == 0) {
                    hideWaitDialog();
                    ToastUtils.showLong(getString(R.string.error_invitecode));
                } else {
                    UserHelper.signUp(userName, psd, email, invitePeopleCode);
                }
            } else {
                hideWaitDialog();
                ToastUtils.showLong(getString(R.string.error_invitecode));
            }
        } else if (obj instanceof LoginEvent) {
            hideWaitDialog();
            LoginEvent loginEvent = (LoginEvent) obj;
            Object[] object = (Object[]) loginEvent.getObject();
            User u = (User) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                ToastUtils.showLong("登录成功");
                LogUtils.i(u.toString());
                enterActivity(this, HomeActivity.class, true);
            } else {
                ToastUtils.showLong("登录失败: " + e.getMessage());
            }
        }
    }
}
