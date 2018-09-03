package succ7.com.filmbase.litepal.model;

import org.litepal.crud.DataSupport;

/**
 * Created by MZIA(527633405@qq.com) on 2016/7/24 18:26
 * 用户实体
 */
public class LitepalUser extends DataSupport {
    private String objectId;
    private String username;
    private String password;
    private long filmCoinCount;//影币个数
    private int level;//用户等级
    private String avatarURL;//头像地址
    private int shareCount;//分享个数
    private int deleteCount;//被删除个数
    private String inviteCode;//用户邀请码

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getFilmCoinCount() {
        return filmCoinCount;
    }

    public void setFilmCoinCount(long filmCoinCount) {
        this.filmCoinCount = filmCoinCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    @Override
    public String toString() {
        return "LitepalUser{" +
                "objectId='" + objectId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", filmCoinCount=" + filmCoinCount +
                ", level=" + level +
                ", avatarURL='" + avatarURL + '\'' +
                ", shareCount=" + shareCount +
                ", deleteCount=" + deleteCount +
                ", inviteCode='" + inviteCode + '\'' +
                '}';
    }
}
