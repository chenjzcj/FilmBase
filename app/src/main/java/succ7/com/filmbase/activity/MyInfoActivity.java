package succ7.com.filmbase.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMShareAPI;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import succ7.com.filmbase.R;
import succ7.com.filmbase.base.ScrollerBaseUIActivity;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.bmob.UserHelper;
import succ7.com.filmbase.eventbus.UpdateUserAvatarEvent;
import succ7.com.filmbase.eventbus.UploadPicEvent;
import succ7.com.filmbase.utils.PhotoUtils;
import succ7.com.filmbase.utils.PicassoUtils;
import succ7.com.filmbase.utils.ShareUtils;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * 我的信息页面
 */
public class MyInfoActivity extends ScrollerBaseUIActivity implements View.OnClickListener {

    private ImageView ivAvatar;
    private Dialog dialog;
    private String avatarURL;
    private TextView username;
    private TextView coin;
    private TextView myres;
    private TextView goodres;
    private TextView level;
    private TextView tvInviteCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mTitleBar.setTitle(getString(R.string.myinfo));
        backListener();
        View view = this.mInflater.inflate(R.layout.activity_my_info, null);
        this.addMainView(view);
        initView(view);
    }

    private void initView(View view) {
        ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
        username = (TextView) view.findViewById(R.id.username);
        coin = (TextView) view.findViewById(R.id.coin);
        myres = (TextView) view.findViewById(R.id.myres);
        goodres = (TextView) view.findViewById(R.id.goodres);
        level = (TextView) view.findViewById(R.id.level);

        tvInviteCode = (TextView) view.findViewById(R.id.tv_inviteCode);
        view.findViewById(R.id.rl_reset_psd).setOnClickListener(this);
        view.findViewById(R.id.rl_bind_phone).setOnClickListener(this);
        view.findViewById(R.id.btn_exit_login).setOnClickListener(this);
        view.findViewById(R.id.btn_invite_friend).setOnClickListener(this);
        setData();
    }

    private void setData() {
        User user = UserHelper.getCurrentUser();
        if (user == null) {
            onBackPressed();
            return;
        }
        //个人信息
        ivAvatar.setOnClickListener(this);
        PicassoUtils.showImage(mBaseActivity, user.getAvatarURL(), ivAvatar);
        username.setText(getString(R.string.my_username, user.getUsername()));
        coin.setText(getString(R.string.coin, user.getFilmCoinCount()));
        myres.setText(getString(R.string.myres, user.getShareCount()));
        goodres.setText(getString(R.string.goodres, (user.getShareCount() - user.getDeleteCount())));
        level.setText(getString(R.string.level, UserHelper.getLevel()));

        tvInviteCode.setText(getString(R.string.my_invite_code, user.getInviteCode()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_avatar:
                showSelectedPhotoDialog();
                break;
            case R.id.btn_exit_login:
                //退出登录
                UserHelper.logout();
                enterActivity(mBaseActivity, LoginActivity.class, true);
                break;
            case R.id.rl_reset_psd:
                //重置密码
                enterActivity(mBaseActivity, UpdatePsdActivity.class, false);
                break;
            case R.id.rl_bind_phone:
                //绑定邮箱
                enterActivity(mBaseActivity, BindEmailActivity.class, false);
                break;
            case R.id.btn_invite_friend:
                ShareUtils.inviteFriend(this);
                break;
        }
    }

    /**
     * 显示选择照片来源对话框
     */
    private void showSelectedPhotoDialog() {
        View view = mInflater.inflate(R.layout.layout_dialog_select_photo, null);
        view.findViewById(R.id.tv_takephoto).setOnClickListener(itemsOnClick);
        view.findViewById(R.id.tv_selectfromgallery).setOnClickListener(itemsOnClick);

        dialog = new Dialog(mBaseActivity);
        dialog.setContentView(view);
        //去掉黑色背景
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            if (dialog != null) {
                dialog.dismiss();
            }
            switch (v.getId()) {
                case R.id.tv_takephoto:
                    PhotoUtils.takePicture(MyInfoActivity.this);
                    break;
                case R.id.tv_selectfromgallery:
                    PhotoUtils.selectFromAlbum(MyInfoActivity.this);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //值得注意的是，分享也应该重写onActivityResult()
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PhotoUtils.PHOTO_PICKED_WITH_DATA:
                if (data != null && data.getData() != null)
                    PhotoUtils.startPhotoZoom(this, data.getData());
                break;
            case PhotoUtils.CAMERA_WITH_DATA:
                if (resultCode != 0)
                    PhotoUtils.startPhotoZoom(this, PhotoUtils.tempuri);
                break;
            case PhotoUtils.CUT_PHOTO:
                /**
                 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃当前功能时，
                 * 会报NullException，只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况 &nbsp;
                 */
                if (data != null)
                    creatWaitDialog(getString(R.string.submit_film_pic)).show();
                PhotoUtils.saveAndUpload(this, data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventMainThread(Object obj) {
        if (obj instanceof UploadPicEvent) {
            hideWaitDialog();
            UploadPicEvent uploadPicEvent = (UploadPicEvent) obj;
            Object[] object = (Object[]) uploadPicEvent.getObject();
            BmobFile bmobFile = (BmobFile) object[0];
            BmobException e = (BmobException) object[1];
            if (e == null) {
                avatarURL = bmobFile.getUrl();
                ToastUtils.showLong("图片上传成功");
                PicassoUtils.showImage(mBaseActivity, avatarURL, R.mipmap.defalut_film, ivAvatar);
                UserHelper.updateUserAvatar(avatarURL);
            } else {
                ToastUtils.showLong("图片上传失败: " + e.getMessage());
            }
        } else if (obj instanceof UpdateUserAvatarEvent) {
            hideWaitDialog();
            UpdateUserAvatarEvent updateUserAvatarEvent = (UpdateUserAvatarEvent) obj;
            BmobException e = (BmobException) updateUserAvatarEvent.getObject();
            if (e == null) {
                ToastUtils.showLong("图片更新成功");
                setData();
            } else {
                ToastUtils.showLong("图片更新失败: " + e.getMessage());
            }
        }
    }
}
