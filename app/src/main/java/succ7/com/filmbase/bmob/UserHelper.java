package succ7.com.filmbase.bmob;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import de.greenrobot.event.EventBus;
import succ7.com.filmbase.AppManager;
import succ7.com.filmbase.bean.bmob.User;
import succ7.com.filmbase.eventbus.CheckInviteCodeEvent;
import succ7.com.filmbase.eventbus.LoginEvent;
import succ7.com.filmbase.eventbus.RegisterEvent;
import succ7.com.filmbase.eventbus.ResetPasswordEvent;
import succ7.com.filmbase.eventbus.UpdateCoinEvent;
import succ7.com.filmbase.eventbus.UpdateEmailEvent;
import succ7.com.filmbase.eventbus.UpdatePsdEvent;
import succ7.com.filmbase.eventbus.UpdateUserAvatarEvent;
import succ7.com.filmbase.eventbus.VerifyEmailEvent;
import succ7.com.filmbase.utils.LogUtils;
import succ7.com.filmbase.utils.SerialNumberUtil;
import succ7.com.filmbase.utils.ToastUtils;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/18 0018 18:05
 * 用户操作帮助类
 */
public class UserHelper {
    //影币获取类型
    public static final int COIN_GET_TYPE_ADD_FILM = 0;//分享资源获取
    public static final int COIN_GET_TYPE_GOOD_FILM = 1;//资源被赞100个获取
    public static final int COIN_GET_TYPE_FILM_DELETE = 2;//资源被删除减去
    public static final int COIN_GET_TYPE_INVITE_USER = 3;//邀请用户获取
    public static final int COIN_GET_TYPE_OTHER = 4;//其他渠道获取

    /**
     * 用户注册
     *
     * @param userName         用户名
     * @param psd              密码
     * @param email            邮箱
     * @param invitePeopleCode 邀请人的邀请码
     */
    public static void signUp(String userName, String psd, String email, String invitePeopleCode) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(psd);
        user.setEmail(email);
        String inviteCode = SerialNumberUtil.toSerialNumber(userName.hashCode());
        user.setInviteCode(inviteCode);
        user.setInvitePeopleCode(invitePeopleCode);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                LogUtils.i("signUp u = " + u + ",e = " + e);
                postEvent(new RegisterEvent(new Object[]{u, e}));
            }
        });
    }

    /**
     * 修改邮箱
     *
     * @param newemail 新邮箱
     */
    public static void updateEmail(String newemail) {
        User user = new User();
        user.setEmail(newemail);
        user.update(getCurrentUseObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("updateEmail e = " + e);
                postEvent(new UpdateEmailEvent(e));
            }
        });
    }

    /**
     * 请求邮箱验证
     *
     * @param email 需要验证的邮箱
     */
    public static void requestEmailVerify(String email) {
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("requestEmailVerify e = " + e);
                postEvent(new VerifyEmailEvent(e));
            }
        });
    }

    /**
     * 邮箱密码重置
     *
     * @param email 通过邮箱重置密码
     */
    public static void resetPasswordByEmail(String email) {
        BmobUser.resetPasswordByEmail(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("resetPasswordByEmail e = " + e);
                postEvent(new ResetPasswordEvent(e));
            }
        });
    }

    /**
     * 检查该邀请码是否合法
     *
     * @param inviteCode 需要验证的邀请码
     */
    public static void checkInviteCodeExist(String inviteCode) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("inviteCode", inviteCode);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                LogUtils.i("isInviteCodeExist list = " + list + ",e = " + e);
                postEvent(new CheckInviteCodeEvent(new Object[]{list, e}));
            }
        });
    }

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param psd      密码
     */
    public static void login(String userName, String psd) {
        User user = new User();
        user.setUsername(userName);
        user.setPassword(psd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User u, BmobException e) {
                LogUtils.i("login u = " + u + ",e = " + e);
                postEvent(new LoginEvent(new Object[]{u, e}));
            }
        });
    }

    /**
     * 退出登录,清除缓存用户对象
     */
    public static void logout() {
        BmobUser.logOut();
        ToastUtils.showLong("退出成功");
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 修改密码
     *
     * @param oldPsd 旧密码
     * @param newPsd 新密码
     */
    public static void updatePassword(String oldPsd, String newPsd) {
        BmobUser.updateCurrentUserPassword(oldPsd, newPsd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("updatePassword e = " + e);
                postEvent(new UpdatePsdEvent(new Object[]{e}));
            }
        });
    }

    /**
     * 更新头像
     *
     * @param avatarURL 头像文件路径
     */
    public static void updateUserAvatar(String avatarURL) {
        User currentUser = UserHelper.getCurrentUser();
        currentUser.setAvatarURL(avatarURL);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("updateUserAvatar e = " + e);
                postEvent(new UpdateUserAvatarEvent(e));
            }
        });
    }

    /**
     * 更新影币个数
     * 1.每分享一条资源,得5个影币;
     * 2.分享的资源得到100个以上赞,得5个影币;
     * 3.分享的资源因被踩或者其他原因被删除,将扣除10个影币;
     * 4.使用邀请码每邀请一个用户注册,将获取30影币;
     * 5.其他渠道获取影币数量将视规则发放;
     * 6.影币可以在影币兑换商城兑换相应的商品;
     * 7.最终解释权归本app所有.
     *
     * @param type 影币获取类型
     */
    public static void updateCoin(int type) {
        User currentUser = UserHelper.getCurrentUser();
        int coinCount = 5;
        switch (type) {
            case COIN_GET_TYPE_ADD_FILM:
                coinCount = 5;
                currentUser.increment("shareCount");
                break;
            case COIN_GET_TYPE_GOOD_FILM:
                coinCount = 5;
                break;
            case COIN_GET_TYPE_FILM_DELETE:
                coinCount = -10;
                break;
            case COIN_GET_TYPE_INVITE_USER:
                coinCount = 30;
                break;
            case COIN_GET_TYPE_OTHER:
                coinCount = 5;
                break;
        }
        currentUser.increment("filmCoinCount", coinCount);
        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                LogUtils.i("updateCoin e = " + e);
                postEvent(new UpdateCoinEvent(e));
            }
        });
    }

    /**
     * 获取当前登录的用户信息,有可能为null
     *
     * @return User
     */
    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }

    /**
     * 获取当前登录的用户信息,有可能为null
     *
     * @return User
     */
    public static String getCurrentUserName() {
        User user = getCurrentUser();
        return user != null ? user.getUsername() : "-";
    }

    /**
     * 获取当前登录的用户对象id,有可能为null
     *
     * @return User
     */
    public static String getCurrentUseObjectId() {
        User user = getCurrentUser();
        return user != null ? user.getObjectId() : null;
    }


    /**
     * 发送异步事件
     *
     * @param obj 事件
     */
    public static void postEvent(Object obj) {
        EventBus.getDefault().post(obj);
    }

    /**
     * 根据影币数量分为五个等级 :
     * 1. 0-15          v1
     * 2.16-35          v2
     * 3.36 - 80        v3
     * 4. 81 - 150      v4
     * 5 . 150+         v5
     *
     * @return 用户等级
     */
    public static String getLevel() {
        User currentUser = getCurrentUser();
        long filmCoinCount = currentUser.getFilmCoinCount();
        if (filmCoinCount <= 15) {
            return "V1";
        } else if (filmCoinCount <= 35) {
            return "V2";
        } else if (filmCoinCount <= 80) {
            return "V3";
        } else if (filmCoinCount <= 150) {
            return "V4";
        } else if (filmCoinCount > 150) {
            return "V5";
        } else {
            return "V1";
        }
    }
}
