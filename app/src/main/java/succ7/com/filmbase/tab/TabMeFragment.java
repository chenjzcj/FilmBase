package succ7.com.filmbase.tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import succ7.com.filmbase.R;
import succ7.com.filmbase.activity.AboutActivity;
import succ7.com.filmbase.activity.CoinGetActivity;
import succ7.com.filmbase.activity.MallActivity;
import succ7.com.filmbase.activity.MyInfoActivity;
import succ7.com.filmbase.activity.MyResActivity;
import succ7.com.filmbase.activity.ShareRemindActivity;
import succ7.com.filmbase.base.TabBaseFragment;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.LoginRequestEvent;
import succ7.com.filmbase.eventbus.UpdateCoinEvent;
import succ7.com.filmbase.utils.PicassoUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/05/17 0016 10:35
 * 我的tab页
 */
public class TabMeFragment extends TabBaseFragment implements View.OnClickListener {

    private RelativeLayout rlMyres;
    private RelativeLayout rlFilmshare;
    private RelativeLayout rlCoinget;
    private RelativeLayout rlMall;
    private RelativeLayout rlAbout;
    private ImageView ivAvatar;
    private TextView username;
    private TextView coin;
    private TextView myres;
    private TextView goodres;
    private TextView level;
    private static int REQUESTCODE = 56;
    private LinearLayout llPersoninfo;
    private TextView tvNotlogin;

    @Override
    public void fragmentView() {
        this.initView(this.getView());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void errorViewListener() {

    }

    private void initView(View view) {
        this.titlebar.setTitle(mContext.getString(R.string.me));
        this.mViewStub.setLayoutResource(R.layout.tab_me);
        this.mViewStub.inflate();
        this.setRightView();

        ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        username = (TextView) view.findViewById(R.id.username);
        coin = (TextView) view.findViewById(R.id.coin);
        myres = (TextView) view.findViewById(R.id.myres);
        goodres = (TextView) view.findViewById(R.id.goodres);
        level = (TextView) view.findViewById(R.id.level);
        llPersoninfo = (LinearLayout) view.findViewById(R.id.ll_personinfo);
        tvNotlogin = (TextView) view.findViewById(R.id.tv_notlogin);
        setData();

        rlMyres = (RelativeLayout) view.findViewById(R.id.rl_myres);
        rlFilmshare = (RelativeLayout) view.findViewById(R.id.rl_filmshare);
        rlCoinget = (RelativeLayout) view.findViewById(R.id.rl_coinget);
        rlMall = (RelativeLayout) view.findViewById(R.id.rl_mall);
        rlAbout = (RelativeLayout) view.findViewById(R.id.rl_about);

        rlMyres.setOnClickListener(this);
        rlFilmshare.setOnClickListener(this);
        rlCoinget.setOnClickListener(this);
        rlMall.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        tvNotlogin.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
    }

    private void setData() {
        User user = UserHelper.getCurrentUser();
        if (user == null) {
            llPersoninfo.setVisibility(View.GONE);
            tvNotlogin.setVisibility(View.VISIBLE);
            return;
        }
        llPersoninfo.setVisibility(View.VISIBLE);
        tvNotlogin.setVisibility(View.GONE);
        PicassoUtils.showImage(mContext, user.getAvatarURL(), ivAvatar);
        username.setText(getString(R.string.my_username, user.getUsername()));
        coin.setText(getString(R.string.coin, user.getFilmCoinCount()));
        myres.setText(getString(R.string.myres, user.getShareCount()));
        goodres.setText(getString(R.string.goodres, (user.getShareCount() - user.getDeleteCount())));
        level.setText(getString(R.string.level, UserHelper.getLevel()));
    }

    /**
     * 设置标题栏右边的View
     */
    private void setRightView() {
        ImageView msgIcShowallwatcher = new ImageView(mContext);
        msgIcShowallwatcher.setImageResource(R.mipmap.ic_myinfo);
        msgIcShowallwatcher.setScaleType(ImageView.ScaleType.CENTER);
        this.titlebar.setRightLayoutListener(msgIcShowallwatcher, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(TabMeFragment.this.getClass().getSimpleName()));
                    return;
                }
                enterActivity(MyInfoActivity.class, REQUESTCODE);
            }
        });
        this.titlebar.setRightViewVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                enterActivity(MyInfoActivity.class, REQUESTCODE);
                break;
            case R.id.rl_myres:
                if (UserHelper.getCurrentUser() == null) {
                    postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                    return;
                }
                enterActivity(MyResActivity.class, -1);
                break;
            case R.id.rl_filmshare:
                enterActivity(ShareRemindActivity.class, -1);
                break;
            case R.id.rl_coinget:
                enterActivity(CoinGetActivity.class, -1);
                break;
            case R.id.rl_mall:
                enterActivity(MallActivity.class, -1);
                break;
            case R.id.rl_about:
                enterActivity(AboutActivity.class, -1);
                break;
            case R.id.tv_notlogin:
                postEvent(new LoginRequestEvent(this.getClass().getSimpleName()));
                break;
        }
    }

    public void enterActivity(Class cls, int requestcode) {
        Intent intent = new Intent(getActivity(), cls);
        if (requestcode != -1) {
            //不能使用getActivity(),不然的话onActivityResult()方法无法被调用,
            // 而是调用了此fragment的父activity的onActivityResult()方法,此处要特别注意哈
            startActivityForResult(intent, requestcode);
        } else {
            startActivity(intent);
        }
        getActivity().overridePendingTransition(R.anim.push_left_acc, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            setData();
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof UpdateCoinEvent) {
            setData();
        }
    }
}
