package succ7.com.filmbase.bean.bmob;

import cn.bmob.v3.BmobObject;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/30 0030 18:39
 * 反馈实体
 */
public class Feedback extends BmobObject {
    private String contact;//联系方式
    private String content;//反馈内容
    private User user;//反馈的用户
    private String deviceInfo;//用户的软件及设备信息

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
