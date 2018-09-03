package succ7.com.filmbase.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.LoginEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.MyTextUtils;
import succ7.com.filmbase.utils.ToastUtils;

public class LoginActivity extends ScrollerBaseUIActivity implements View.OnClickListener {

    private EditText etPsd;
    private EditText etUsername;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvForgotpassword;
    private String userName;
    private String psd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.user_login));
        View view = this.mInflater.inflate(R.layout.activity_login, null);
        this.addMainView(view);
        initView(view);
    }

    protected void initView(View view) {
        etUsername = (EditText) view.findViewById(R.id.et_username);
        etPsd = (EditText) view.findViewById(R.id.et_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        tvRegister = (TextView) view.findViewById(R.id.tv_register);
        tvForgotpassword = (TextView) view.findViewById(R.id.tv_forgotpassword);
        view.findViewById(R.id.tv_nologin).setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgotpassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                enterActivity(this, RegisterActivity.class, false);
                break;
            case R.id.tv_forgotpassword:
                enterActivity(this, ForgotPsdActivity.class, false);
                break;
            case R.id.tv_nologin:
                enterActivity(this, HomeActivity.class, true);
                break;
        }
    }

    /**
     * 登录
     */
    public void login() {
        userName = etUsername.getText().toString();
        psd = etPsd.getText().toString();
        if (MyTextUtils.isEmpty(userName) && MyTextUtils.isEmpty(psd)) {
            ToastUtils.showShortToast(mBaseActivity, getString(R.string.input_right_usename_and_psd));
        } else {
            creatWaitDialog(getString(R.string.logining)).show();
            UserHelper.login(userName, psd);
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof LoginEvent) {
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
