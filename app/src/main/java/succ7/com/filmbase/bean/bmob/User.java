package succ7.com.filmbase.bean.bmob;

import cn.bmob.v3.BmobUser;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 18:26
 * 用户实体
 */
public class User extends BmobUser {
    private long filmCoinCount;//影币个数
    private String avatarURL;//头像地址
    private int shareCount;//分享个数
    private int deleteCount;//被删除个数
    private String inviteCode;//用户邀请码
    private String invitePeopleCode;//邀请人的邀请码

    public long getFilmCoinCount() {
        return filmCoinCount;
    }

    public void setFilmCoinCount(long filmCoinCount) {
        this.filmCoinCount = filmCoinCount;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }


    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public int getDeleteCount() {
        return deleteCount;
    }

    public void setDeleteCount(int deleteCount) {
        this.deleteCount = deleteCount;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInvitePeopleCode() {
        return invitePeopleCode;
    }

    public void setInvitePeopleCode(String invitePeopleCode) {
        this.invitePeopleCode = invitePeopleCode;
    }
}
