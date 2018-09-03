package succ7.com.filmbase.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/16 0016 15:36
 * 版本更新信息
 */
public class UpdateInfo extends BmobObject {
    private String apkDownloadUrl;//最新版本下载地址
    private String versionName;//版本名
    private String versionCode;//版本号
    private String content;//更新内容
    private boolean isForce;//是否强制更新

    public String getApkDownloadUrl() {
        return apkDownloadUrl;
    }

    public void setApkDownloadUrl(String apkDownloadUrl) {
        this.apkDownloadUrl = apkDownloadUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "apkDownloadUrl='" + apkDownloadUrl + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", content='" + content + '\'' +
                ", isForce=" + isForce +
                '}';
    }
}
